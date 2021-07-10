package se.lindhen.qrgame.bytecode;

import se.lindhen.qrgame.program.*;
import se.lindhen.qrgame.program.functions.UserFunction;
import se.lindhen.qrgame.program.statements.ForEachStatement;
import se.lindhen.qrgame.program.statements.IfStatement;
import se.lindhen.qrgame.program.statements.WhileStatement;
import se.lindhen.qrgame.program.expressions.*;
import se.lindhen.qrgame.program.objects.QgClass;
import se.lindhen.qrgame.program.statements.*;
import se.lindhen.qrgame.program.types.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class QgDecompiler {

    private static final int VERSION = 1;
    private Program program;
    private final BitReader reader;
    private final HashMap<Integer, Type> variableTypeMap = new HashMap<>();
    private final HashMap<Integer, QgClass<?>> classIdMap = new HashMap<>();
    private final HashMap<Integer, UserFunctionData> userFunctionData = new HashMap<>();

    public QgDecompiler(byte[] data) {
        this.reader = new BitReader(data);
        PredefinedClasses.getClasses().forEach(clazz -> classIdMap.put(clazz.id, clazz.clazz));
    }

    public Program decompile() {
        program = new Program();
        int version = reader.read(4);
        if (version != VERSION) {
            throw new RuntimeException("Cannot read version " + version);
        }
        ArrayList<StructDefinition> structs = decompileStructs();
        ArrayList<UserFunction> userFunctions = decompileFunctions();
        Statement init = decompileStatement();
        Statement code = decompileStatement();
        InputCode inputCode = decompileInputCode();
        program.setStructDefinitions(structs);
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
        ArrayList<ArrayList<UserFunction.UserFunctionParameter>> parameters = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Type returnType = decompileType();
            int parameterCount = reader.readPositiveByte();
            ArrayList<UserFunction.UserFunctionParameter> functionParameters = new ArrayList<>();
            for (int p = 0; p < parameterCount; p++) {
                functionParameters.add(new UserFunction.UserFunctionParameter(new Variable(reader.readPositiveByte(), decompileType(), true)));
            }
            userFunctionData.put(i, new UserFunctionData(returnType, functionParameters.size()));
            parameters.add(functionParameters);
        }
        ArrayList<UserFunction> functions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Statement body = decompileStatement();
            functions.add(new UserFunction(i, parameters.get(i), userFunctionData.get(i).getReturnType(), body));
        }
        return functions;
    }

    private ArrayList<StructDefinition> decompileStructs() {
        int count = reader.readPositiveByte();
        ArrayList<StructDefinition> structs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            StructDefinition structDef = program.addStructDefinition();
            int numFields = reader.readPositiveByte();
            ArrayList<Type> fields = new ArrayList<>();
            for (int j = 0; j < numFields; j++) {
                fields.add(decompileType());
            }
            structDef.setFields(fields);
        }
        return structs;
    }

    private Type decompileType() {
        TypeCode typeCode = TypeCode.fromCode(reader.read(4));
        switch (typeCode) {
            case NUMBER: return NumberType.NUMBER_TYPE;
            case BOOL: return BoolType.BOOL_TYPE;
            case STRUCT: return new StructType(reader.readPositiveByte());
            case OBJECT:
                int classId = reader.readPositiveByte();
                int numTypeArgs = reader.readPositiveByte();
                Type[] typeArgs = new Type[numTypeArgs];
                for (int i = 0; i < numTypeArgs; i++) {
                    typeArgs[i] = decompileType();
                }
                return new ObjectType(classIdMap.get(classId), typeArgs);
            default:
                throw new IllegalStateException("Could not decompile type with code " + typeCode);
        }
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
                Expression toIterate = decompileExpression();
                variableTypeMap.put(var, ((ObjectType) toIterate.getType()).getQgClass().iteratorType((ObjectType) toIterate.getType()));
                return new ForEachStatement(toIterate, var, decompileStatement(), hasForEachLabel ? reader.readPositiveByte() : null);
            case WHEN:
                boolean hasDefault = reader.readBool();
                WhenStatementBuilder builder = new WhenStatementBuilder();
                if (hasDefault) {
                    builder.setDefaultCase(decompileStatement());
                }
                builder.setToCompare(decompileExpression());
                int clauseCount = reader.readPositiveByte();
                for (int i = 0; i < clauseCount; i++) {
                    builder.putClause(readWhenClause(builder.getParameterType()), decompileStatement());
                }
                return builder.build();
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

    private Object readWhenClause(Type type) {
        switch (type.getBaseType()) {
            case NUMBER:
                return (double) reader.readInt();
            case BOOL:
                return reader.readBool();
            default:
                throw new RuntimeException("Can only deserialize when clauses with number or bool.");
        }
    }

    private Expression decompileExpression() {
        return decompileExpression(reader.readCommand(), false);
    }

    private Expression decompileExpression(CommandCode commandCode, boolean isFallback) {
        switch (commandCode) {
            case GET_VARIABLE:
                int varId = reader.readPositiveByte();
                Type type = variableTypeMap.get(varId);
                return new VariableExpression(new Variable(varId, type, false));
            case STACK_VAR:
                int stackVarId = reader.readPositiveByte();
                Type stackType = variableTypeMap.get(stackVarId);
                return new StackVariableExpression(new Variable(stackVarId, stackType, true));
            case LITERAL_NUMBER:
                return new NumberExpression(reader.readInt());
            case EQUALS:
                return new EqualsExpression(decompileExpression(), decompileExpression());
            case LITERAL_BOOL:
                return new BoolExpression(reader.readBool());
            case LITERAL_NULL:
                return new NullExpression(decompileType());
            case ADD:
                return new AddExpression(decompileExpression(), decompileExpression());
            case FUNCTION:
                boolean userFunction = reader.readBool();
                int funcId = reader.readInt();
                int argCount = userFunction ?
                        userFunctionData.get(funcId).getParameterCount() :
                        program.getFunction(funcId).getConstantParameterCount().orElseGet(reader::readPositiveByte);
                ArrayList<Expression> args = new ArrayList<>();
                for (int i = 0; i < argCount; i++) {
                    args.add(decompileExpression());
                }
                Type returnType = userFunction ? userFunctionData.get(funcId).getReturnType() : program.getFunction(funcId).getReturnType(args);
                return new FunctionExpression(funcId, returnType, args, userFunction);
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
                Optional<Integer> constantParameterCount = ((ObjectType) objectExpression.getType()).getQgClass()
                        .getMethod(methodId)
                        .getConstantParameterCount();
                int argCount2 = constantParameterCount.orElseGet(reader::readPositiveByte);
                ArrayList<Expression> args2 = new ArrayList<>();
                for (int i = 0; i < argCount2; i++) {
                    args2.add(decompileExpression());
                }
                return new MethodCallExpression(objectExpression, methodId, args2);
            case TYPE_FETCH:
                return new TypeFetchExpression(decompileExpression());
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
                int structId2 = ((StructType) structExpression.getType()).getStructId();
                Type fieldType = program.getStructDefinition(structId2).getFields().get(fieldId);
                return new StructFetchExpression(structExpression, fieldType, fieldId);
            case MODULO:
                return new ModulusExpression(decompileExpression(), decompileExpression());
            case ASSIGN:
                int varIdd = reader.readPositiveByte();
                Expression expression = decompileExpression();
                variableTypeMap.put(varIdd, expression.getType());
                return new AssignExpression(varIdd, expression);
            case GET_AND_MODIFY:
                int varIddd = reader.readPositiveByte();
                boolean incElseDec = reader.readBool();
                return new GetAndModifyExpression(varIddd, incElseDec);
            case STRUCT_ASSIGN:
                int structField = reader.readPositiveByte();
                return new StructAssignExpression(decompileExpression(), structField, decompileExpression());
            case CONDITIONAL:
                return new ConditionalExpression(decompileExpression(), decompileExpression(), decompileExpression());
            default:
                throw new UnrecognizedCommandException("Received unexpected command " + commandCode + " when decompiling expression.", isFallback);
        }
    }
}
