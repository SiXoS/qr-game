package se.lindhen.qrgame.bytecode;

import se.lindhen.qrgame.program.*;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.functions.UserFunction;
import se.lindhen.qrgame.program.statements.ForEachStatement;
import se.lindhen.qrgame.program.statements.IfStatement;
import se.lindhen.qrgame.program.statements.WhileStatement;
import se.lindhen.qrgame.program.expressions.*;
import se.lindhen.qrgame.program.statements.*;
import se.lindhen.qrgame.program.types.Type;

import java.util.*;

import static se.lindhen.qrgame.bytecode.CommandCode.*;

public class QgCompiler {

    private static final int VERSION = 2;
    private final BitWriter writer = new BitWriter();
    private final Program program;
    private final HashMap<String, Integer> classNameToIdMap = new HashMap<>();

    public QgCompiler(Program program) {
        this.program = program;
        PredefinedClasses.getClasses()
                .forEach(predefinedClass -> classNameToIdMap.put(predefinedClass.clazz.getName(), predefinedClass.id));
    }

    public Map<String, Integer> getContextualBitsWritten() {
        return writer.getContextualBitsWritten();
    }

    public byte[] compile() {
        writer.write(4, VERSION);
        compileFunctions(program.getUserFunctions());
        compileStatement(program.getInitialisation());
        compileStatement(program.getCode());
        compileInputCode(program.getInputCode());
        return writer.getBuffer();
    }

    private void compileInputCode(InputCode inputCode) {
        writer.setContext("inputCode");
        writer.writePositiveByte(inputCode.getButtonIdVar());
        writer.writePositiveByte(inputCode.getPressedElseReleasedVarId());
        writer.exitContext();
        compileStatement(inputCode.getCode());
    }

    private void compileFunctions(ArrayList<UserFunction> userFunctions) {
        writer.setContext("userFunction");
        writer.writePositiveByte(userFunctions.size());
        for (UserFunction function : userFunctions) {
            Optional<Integer> constantParameterCount = function.getConstantParameterCount();
            writer.writeBool(constantParameterCount.isPresent());
            constantParameterCount.ifPresent(writer::writePositiveByte);
        }
        for (int i = 0; i < userFunctions.size(); i++) {
            assert i == userFunctions.get(i).getId();
            UserFunction userFunction = userFunctions.get(i);
            writer.writePositiveByte(userFunction.getNumberOfTrueArguments());
            writer.writePositiveByte(userFunction.getNumVariables());
            writer.exitContext();
            compileStatement(userFunction.getBody());
            writer.setContext("userFunction");
        }
        writer.exitContext();
    }

    private void compileStatement(Statement statement) {
        if (statement instanceof IfStatement) {
            IfStatement ifStatement = (IfStatement) statement;
            writer.setContext("if");
            writer.writeCommand(IF);
            writer.writeBool(ifStatement.hasElseBody());
            writer.exitContext();
            compileExpression(ifStatement.getCondition());
            compileStatement(ifStatement.getBody());
            if (ifStatement.hasElseBody()) {
                compileStatement(ifStatement.getElseBody());
            }
        } else if (statement instanceof WhileStatement) {
            WhileStatement whileStatement = (WhileStatement) statement;
            writer.setContext("while");
            writer.writeCommand(WHILE);
            writer.writeBool(whileStatement.hasPostBody());
            writer.writeBool(whileStatement.hasLabel());
            writer.exitContext();
            compileExpression(whileStatement.getCondition());
            compileStatement(whileStatement.getBody());
            if (whileStatement.hasPostBody()) {
                compileStatement(whileStatement.getPostBody());
            }
            if (whileStatement.hasLabel()) {
                writer.writePositiveByte(whileStatement.getLabel());
            }
        } else if (statement instanceof BlockStatement) {
            if (((BlockStatement) statement).getStatements().size() == 1) {
                compileStatement(((BlockStatement) statement).getStatements().get(0));
            } else {
                writer.setContext("block");
                writer.writeCommand(BLOCK);
                ArrayList<Statement> statements = ((BlockStatement) statement).getStatements();
                writer.writePositiveByte(statements.size());
                writer.exitContext();
                statements.forEach(this::compileStatement);
            }
        } else if (statement instanceof ExpressionStatement) {
            compileExpression(((ExpressionStatement) statement).getExpression());
        } else if (statement instanceof ForEachStatement) {
            writer.setContext("for");
            writer.writeCommand(FOREACH);
            ForEachStatement forEachStatement = (ForEachStatement) statement;
            writer.writePositiveByte(forEachStatement.getTargetVarId());
            writer.writeBool(forEachStatement.hasLabel());
            writer.writeBool(forEachStatement.isOnStack());
            writer.exitContext();
            compileExpression(forEachStatement.getToIterate());
            compileStatement(forEachStatement.getBody());
            if (forEachStatement.hasLabel()) {
                writer.writePositiveByte(forEachStatement.getLabel());
            }
        } else if (statement instanceof WhenStatement) {
            WhenStatement whenStatement = (WhenStatement) statement;
            writer.setContext("when");
            writer.writeCommand(WHEN);
            writer.writeBool(whenStatement.getDefaultCase() != null);
            Type toCompareType = whenStatement.getToCompare().getType();
            compileBaseType(toCompareType);
            writer.exitContext();
            if (whenStatement.getDefaultCase() != null) {
                compileStatement(whenStatement.getDefaultCase());
            }
            compileExpression(whenStatement.getToCompare());
            writer.writePositiveByte(whenStatement.getCases().size());
            whenStatement.getCases().forEach((aCase, caseBody) -> {
                compileWhenCase(toCompareType, aCase);
                compileStatement(caseBody);
            });
        } else if (statement instanceof ReturnStatement) {
            writer.setContext("return");
            writer.writeCommand(RETURN);
            boolean hasValue = ((ReturnStatement) statement).hasValue();
            writer.writeBool(hasValue);
            writer.exitContext();
            if (hasValue) {
                compileExpression(((ReturnStatement) statement).getValue());
            }
        } else if (statement instanceof InterruptStatement) {
            InterruptStatement interruptStatement = (InterruptStatement) statement;
            writer.setContext("interrupt");
            writer.writeCommand(INTERRUPT);
            writer.writeBool(interruptStatement.isBreakElseContinue());
            writer.writeBool(interruptStatement.hasLabel());
            if (interruptStatement.hasLabel()) {
                writer.writePositiveByte(interruptStatement.getLabel());
            }
            writer.exitContext();
        } else {
            throw new UnsupportedOperationException("Compilation of statement " + statement.getClass().getSimpleName() + " not implemented.");
        }
    }

    private void compileBaseType(Type type) {
        writer.write(4, TypeCode.fromType(type).getCode());
    }

    private void compileWhenCase(Type type, Object aCase) {
        switch (type.getBaseType()) {
            case NUMBER:
                writer.writeInt((int)(double) aCase);
                return;
            case BOOL:
                writer.writeBool((boolean) aCase);
                return;
            default:
                throw new RuntimeException("Can only compile when cases for number and bool");
        }
    }

    private void compileExpression(Expression expression) {
        if (expression instanceof NumberExpression) {
            writer.setContext("number");
            writer.writeCommand(LITERAL_NUMBER);
            double number = ((NumberExpression) expression).getNumber();
            if (isLong(number)) {
                writer.writeBool(true);
                writer.exitContext();
                writer.setContext("integerNumber");
                writer.writeLong((long) number);
                writer.exitContext();
            } else {
                writer.writeBool(false);
                writer.exitContext();
                writer.setContext("irrationalNumber");
                writer.writeDouble(number);
                writer.exitContext();
            }
        } else if (expression instanceof AddExpression) {
            writer.setContext("add");
            writer.writeCommand(ADD);
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else if (expression instanceof AndExpression) {
            writer.setContext("and");
            writer.writeCommand(AND);
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else if (expression instanceof BoolExpression) {
            writer.setContext("bool");
            writer.writeCommand(LITERAL_BOOL);
            writer.writeBool(((BoolExpression) expression).getBool());
            writer.exitContext();
        } else if (expression instanceof DivideExpression) {
            writer.setContext("divide");
            writer.writeCommand(DIVIDE);
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else if (expression instanceof EqualsExpression) {
            writer.setContext("equals");
            writer.writeCommand(EQUALS);
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else if (expression instanceof GreaterThanExpression) {
            writer.setContext("greater");
            writer.writeCommand(GREATER);
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else if (expression instanceof LessThanExpression) {
            writer.setContext("less");
            writer.writeCommand(LESS);
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else if (expression instanceof MethodCallExpression) {
            MethodCallExpression methodCallExpression = (MethodCallExpression) expression;
            writer.setContext("method");
            writer.writeCommand(METHOD_CALL);
            writer.exitContext();
            compileExpression(methodCallExpression.getObjectExpression());
            writer.setContext("method");
            writer.writePositiveByte(methodCallExpression.getMethodId());
            writer.writePositiveByte(methodCallExpression.getArgumentExpressions().size());
            writer.exitContext();
            compileExpressions(methodCallExpression.getArgumentExpressions());
        } else if (expression instanceof MultiplicativeExpression) {
            writer.setContext("multiply");
            writer.writeCommand(MULTIPLY);
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else if (expression instanceof NegateExpression) {
            writer.setContext("negate");
            writer.writeCommand(NEGATE);
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else if (expression instanceof OrExpression) {
            writer.setContext("or");
            writer.writeCommand(OR);
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else if (expression instanceof TypeExpression) {
            writer.setContext("typeExpression");
            writer.writeCommand(TYPE_EXPRESSION);
            writer.exitContext();
        } else if (expression instanceof VariableExpression) {
            writer.setContext("getVariable");
            writer.writeCommand(GET_VARIABLE);
            writer.writePositiveByte(((VariableExpression) expression).getVarId());
            writer.exitContext();
        } else if (expression instanceof StackVariableExpression) {
            writer.setContext("stackVariable");
            writer.writeCommand(STACK_VAR);
            writer.writePositiveByte(((StackVariableExpression) expression).getVarId());
            writer.exitContext();
        } else if (expression instanceof FunctionExpression) {
            writer.setContext("functionCall");
            writer.writeCommand(FUNCTION);
            FunctionExpression functionExpression = (FunctionExpression) expression;
            writer.writeBool(functionExpression.isUserFunction());
            writer.writeInt(functionExpression.getFunctionId());
            Function function = functionExpression.isUserFunction() ?
                    program.getUserFunction(functionExpression.getFunctionId()) :
                    program.getFunction(functionExpression.getFunctionId());
            if (!function.getConstantParameterCount().isPresent()) {
                writer.writePositiveByte(expression.getSubExpressions().size());
            }
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else if (expression instanceof NotExpression) {
            writer.setContext("not");
            writer.writeCommand(NOT);
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else if (expression instanceof StructInstantiationExpression) {
            writer.setContext("newStruct");
            writer.writeCommand(NEW_STRUCT);
            writer.writeInt(((StructInstantiationExpression) expression).getStructId());
            writer.writeInt(expression.getSubExpressions().size());
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else if (expression instanceof StructFetchExpression) {
            writer.setContext("structFetch");
            writer.writeCommand(STRUCT_FETCH);
            writer.writePositiveByte(((StructFetchExpression) expression).getFieldId());
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else if (expression instanceof ModulusExpression) {
            writer.setContext("modulo");
            writer.writeCommand(MODULO);
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else if (expression instanceof AssignExpression) {
            writer.setContext("assign");
            writer.writeCommand(ASSIGN);
            writer.writePositiveByte(((AssignExpression) expression).getAssignTo());
            writer.writeBool(((AssignExpression) expression).isOnStack());
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else if (expression instanceof GetAndModifyExpression) {
            writer.setContext("getAndModify");
            writer.writeCommand(GET_AND_MODIFY);
            writer.writePositiveByte(((GetAndModifyExpression) expression).getVarId());
            writer.writeBool(((GetAndModifyExpression) expression).isOnStack());
            writer.writeBool(((GetAndModifyExpression) expression).isIncrementElseDecrement());
            writer.exitContext();
        } else if (expression instanceof StructAssignExpression) {
            StructAssignExpression structAssignExpression = (StructAssignExpression) expression;
            writer.setContext("structAssign");
            writer.writeCommand(STRUCT_ASSIGN);
            writer.writePositiveByte(structAssignExpression.getFieldId());
            writer.exitContext();
            compileExpression(structAssignExpression.getStructExpression());
            compileExpression(structAssignExpression.getValueExpression());
        } else if (expression instanceof NullExpression) {
            writer.setContext("literalNull");
            writer.writeCommand(LITERAL_NULL);
            writer.exitContext();
        } else if (expression instanceof ConditionalExpression) {
            writer.setContext("conditional");
            writer.writeCommand(CONDITIONAL);
            writer.exitContext();
            ConditionalExpression conditionalExpression = (ConditionalExpression) expression;
            compileExpression(conditionalExpression.getCondition());
            compileExpression(conditionalExpression.getTrueCase());
            compileExpression(conditionalExpression.getFalseCase());
        } else if (expression instanceof WhenExpression) {
            WhenExpression whenStatement = (WhenExpression) expression;
            writer.setContext("when");
            writer.writeCommand(WHEN);
            writer.writeBool(whenStatement.getDefaultCase() != null);
            Type toCompareType = whenStatement.getToCompare().getType();
            compileBaseType(toCompareType);
            writer.exitContext();
            if (whenStatement.getDefaultCase() != null) {
                compileExpression(whenStatement.getDefaultCase());
            }
            compileExpression(whenStatement.getToCompare());
            writer.writePositiveByte(whenStatement.getCases().size());
            whenStatement.getCases().forEach((aCase, caseBody) -> {
                compileWhenCase(toCompareType, aCase);
                compileExpression(caseBody);
            });
        } else if (expression instanceof FunctionReferenceExpression) {
            writer.setContext("functionRef");
            writer.writeCommand(FUNCTION_REFERENCE);
            writer.writeInt(((FunctionReferenceExpression) expression).getFunctionId());
            writer.writeBool(((FunctionReferenceExpression) expression).isUserFunction());
            writer.exitContext();
        } else if (expression instanceof FunctionReferenceInvocationExpression) {
            writer.setContext("functionRefInvocation");
            writer.writeCommand(REFERENCE_INVOCATION);
            writer.writePositiveByte(expression.getSubExpressions().size() - 1);
            writer.exitContext();
            compileExpressions(expression.getSubExpressions());
        } else {
            throw new UnsupportedOperationException("Expression not implemented: " + expression.getClass().getName());
        }
    }

    private boolean isLong(double number) {
        return number == (long) number;
    }

    private void compileExpressions(Iterable<Expression> subExpressions) {
        subExpressions.forEach(this::compileExpression);
    }

    public int getBytesWritten() {
        return writer.getBytesWritten();
    }
}
