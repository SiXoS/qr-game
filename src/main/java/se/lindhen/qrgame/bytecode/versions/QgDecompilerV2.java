package se.lindhen.qrgame.bytecode.versions;

import se.lindhen.qrgame.bytecode.BitReader;
import se.lindhen.qrgame.bytecode.CommandCode;
import se.lindhen.qrgame.bytecode.TypeCode;
import se.lindhen.qrgame.bytecode.UnrecognizedCommandException;
import se.lindhen.qrgame.program.InputCode;
import se.lindhen.qrgame.program.PredefinedClasses;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.Variable;
import se.lindhen.qrgame.program.expressions.*;
import se.lindhen.qrgame.program.functions.UserFunction;
import se.lindhen.qrgame.program.objects.QgClass;
import se.lindhen.qrgame.program.statements.*;
import se.lindhen.qrgame.program.types.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class QgDecompilerV2 {

    private Program program;
    private final BitReader reader;
    private final HashMap<Integer, QgClass<?>> classIdMap = new HashMap<>();
    private final HashMap<Integer, Optional<Integer>> userFunctionArgCount = new HashMap<>();

    public QgDecompilerV2(BitReader reader) {
        this.reader = reader;
        PredefinedClasses.getClasses().forEach(clazz -> classIdMap.put(clazz.id, clazz.clazz));
    }

    public Program decompile() {
        program = new Program();
        ArrayList<UserFunction> userFunctions = decompileFunctions();
        Statement init = decompileStatement();
        Statement code = decompileStatement();
        InputCode inputCode = decompileInputCode();
        program.setUserFunctions(userFunctions);
        program.setInitStatement(init);
        program.setCode(code);
        program.setInputCode(inputCode);
        return program;
    }

    private InputCode decompileInputCode() {
        return new InputCode(reader.readPositiveByte(), reader.readPositiveByte(), decompileStatement());
    }

    private ArrayList<UserFunction> decompileFunctions() {
        int count = reader.readPositiveByte();
        for (int i = 0; i < count; i++) {
            boolean hasConstantArgCount = reader.readBool();
            Optional<Integer> constantArgCount = hasConstantArgCount ? Optional.of(reader.readPositiveByte()): Optional.empty();
            userFunctionArgCount.put(i, constantArgCount);
        }
        ArrayList<UserFunction> functions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int numberOfTrueArguments = reader.readPositiveByte();
            int numVariables = reader.readPositiveByte();
            Statement body = decompileStatement();
            functions.add(new UserFunction(i, numberOfTrueArguments, userFunctionArgCount.get(i), numVariables, body));
        }
        return functions;
    }

    private Type.BaseType decompileBaseType() {
        return TypeCode.fromCode((int) reader.read(4)).toBaseType();
    }

    private Statement decompileStatement() {
        CommandCode code = reader.readCommand();
        switch (code) {
            case IF:
                boolean hasElse = reader.readBool();
                Expression condition = decompileExpression();
                Statement main = decompileStatement();
                IfStatement ifStatement = new IfStatement(condition, main);
                if (hasElse) {
                    ifStatement.addFalseBody(decompileStatement());
                }
                return ifStatement;
            case WHILE:
                boolean hasPostBody = reader.readBool();
                boolean hasLabel = reader.readBool();
                return new WhileStatement(decompileExpression(), decompileStatement(),
                        hasPostBody ? decompileStatement() : null, hasLabel ? reader.readPositiveByte() : null);
            case BLOCK:
                int count = reader.readPositiveByte();
                ArrayList<Statement> subStatements = new ArrayList<>(count);
                for (int i = 0; i < count; i++) {
                    subStatements.add(decompileStatement());
                }
                return new BlockStatement(subStatements);
            case EXPRESSION_STATEMENT:
                return new ExpressionStatement(decompileExpression());
            case FOREACH:
                int var = reader.readPositiveByte();
                boolean hasForEachLabel = reader.readBool();
                boolean onStack = reader.readBool();
                Expression toIterate = decompileExpression();
                return new ForEachStatement(toIterate, var, onStack, decompileStatement(), hasForEachLabel ? reader.readPositiveByte() : null);
            case WHEN:
                boolean hasDefault = reader.readBool();
                Type.BaseType toCompare = decompileBaseType();
                WhenBuilder builder = WhenBuilder.whenStatementBuilder();
                if (hasDefault) {
                    builder.setDefaultCase(decompileStatement());
                }
                builder.setToCompare(decompileExpression());
                int clauseCount = reader.readPositiveByte();
                for (int i = 0; i < clauseCount; i++) {
                    builder.putClause(readWhenClause(toCompare), decompileStatement());
                }
                return builder.buildStatement();
            case RETURN:
                boolean hasValue = reader.readBool();
                if (hasValue) {
                    return new ReturnStatement(decompileExpression());
                } else {
                    return new ReturnStatement();
                }
            case INTERRUPT:
                return new InterruptStatement(reader.readBool(), reader.readBool() ? reader.readPositiveByte() : null);
            default:
                try {
                    return new ExpressionStatement(decompileExpression(code, true));
                } catch (UnrecognizedCommandException e) {
                    if (e.isFallback()) {
                        throw new UnrecognizedCommandException("Command was not statement nor expression. " + e.getMessage(), false, e);
                    } else {
                        throw e;
                    }
                }
        }
    }

    private Object readWhenClause(Type.BaseType type) {
        switch (type) {
            case NUMBER:
                return (double) reader.readInt();
            case BOOL:
                return reader.readBool();
            default:
                throw new RuntimeException("Can only deserialize when clauses with number or bool, was " + type + ".");
        }
    }

    private Expression decompileExpression() {
        return decompileExpression(reader.readCommand(), false);
    }

    private Expression decompileExpression(CommandCode commandCode, boolean isFallback) {
        switch (commandCode) {
            case GET_VARIABLE:
                int varId = reader.readPositiveByte();
                return new VariableExpression(new Variable(varId, null, false));
            case STACK_VAR:
                int stackVarId = reader.readPositiveByte();
                return new StackVariableExpression(new Variable(stackVarId, null, true));
            case LITERAL_NUMBER:
                return new NumberExpression(reader.readBool() ? reader.readLong() : reader.readDouble());
            case EQUALS:
                return new EqualsExpression(decompileExpression(), decompileExpression());
            case LITERAL_BOOL:
                return new BoolExpression(reader.readBool());
            case LITERAL_NULL:
                return new NullExpression(null);
            case ADD:
                return new AddExpression(decompileExpression(), decompileExpression());
            case FUNCTION:
                boolean userFunction = reader.readBool();
                int funcId = reader.readInt();
                int argCount = userFunction ?
                        userFunctionArgCount.get(funcId).orElseGet(reader::readPositiveByte) :
                        program.getFunction(funcId).getConstantParameterCount().orElseGet(reader::readPositiveByte);
                ArrayList<Expression> args = new ArrayList<>();
                for (int i = 0; i < argCount; i++) {
                    Expression expression = decompileExpression();
                    args.add(expression);
                }
                return new FunctionExpression(funcId, null, args, userFunction);
            case MULTIPLY:
                return new MultiplicativeExpression(decompileExpression(), decompileExpression());
            case DIVIDE:
                return new DivideExpression(decompileExpression(), decompileExpression());
            case LESS:
                return new LessThanExpression(decompileExpression(), decompileExpression());
            case GREATER:
                return new GreaterThanExpression(decompileExpression(), decompileExpression());
            case AND:
                return new AndExpression(decompileExpression(), decompileExpression());
            case OR:
                return new OrExpression(decompileExpression(), decompileExpression());
            case NOT:
                return new NotExpression(decompileExpression());
            case METHOD_CALL:
                Expression objectExpression = decompileExpression();
                int methodId = reader.readPositiveByte();
                int argCount2 = reader.readPositiveByte();
                ArrayList<Expression> args2 = new ArrayList<>();
                for (int i = 0; i < argCount2; i++) {
                    args2.add(decompileExpression());
                }
                return new MethodCallExpression(objectExpression, methodId, args2, null);
            case TYPE_EXPRESSION:
                return new TypeExpression(null); // types are only used compile-time
            case NEGATE:
                return new NegateExpression(decompileExpression());
            case NEW_STRUCT:
                int structId = reader.readInt();
                int numArguments = reader.readInt();
                Expression[] arguments = new Expression[numArguments];
                for (int i = 0; i < numArguments; i++) {
                    arguments[i] = decompileExpression();
                }
                return new StructInstantiationExpression(structId, arguments);
            case STRUCT_FETCH:
                int fieldId = reader.readPositiveByte();
                Expression structExpression = decompileExpression();
                return new StructFetchExpression(structExpression, null, fieldId);
            case MODULO:
                return new ModulusExpression(decompileExpression(), decompileExpression());
            case ASSIGN:
                return new AssignExpression(reader.readPositiveByte(), reader.readBool(), decompileExpression());
            case GET_AND_MODIFY:
                return new GetAndModifyExpression(reader.readPositiveByte(), reader.readBool(), reader.readBool());
            case STRUCT_ASSIGN:
                int structField = reader.readPositiveByte();
                return new StructAssignExpression(decompileExpression(), structField, decompileExpression());
            case CONDITIONAL:
                return new ConditionalExpression(decompileExpression(), decompileExpression(), decompileExpression());
            case WHEN:
                boolean hasDefault = reader.readBool();
                Type.BaseType toCompareType = decompileBaseType();
                WhenBuilder builder = WhenBuilder.whenExpressionBuilder();
                if (hasDefault) {
                    builder.setDefaultCase(decompileExpression());
                }
                builder.setToCompare(decompileExpression());
                int clauseCount = reader.readPositiveByte();
                for (int i = 0; i < clauseCount; i++) {
                    builder.putClause(readWhenClause(toCompareType), decompileExpression());
                }
                return builder.buildExpression();
            default:
                throw new UnrecognizedCommandException("Received unexpected command " + commandCode + " when decompiling expression.", isFallback);
        }
    }
}
