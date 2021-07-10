package se.lindhen.qrgame.program.functions;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.Variable;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.statements.Statement;
import se.lindhen.qrgame.program.types.Type;

import java.util.ArrayList;
import java.util.Optional;

public class UserFunction extends Function {

    private final int id;
    private final ArrayList<UserFunctionParameter> parameters;
    private final Type[] argumentTypes;
    private final Type returnType;
    private final Statement body;

    public UserFunction(int id, ArrayList<UserFunctionParameter> parameters, Type returnType, Statement body) {
        super("_user_function_" + id);
        this.id = id;
        this.parameters = parameters;
        this.argumentTypes = new Type[parameters.size()];
        for (int i = 0; i < argumentTypes.length; i++) {
            argumentTypes[i] = parameters.get(i).getType();
        }
        this.returnType = returnType;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public ArrayList<UserFunctionParameter> getParameters() {
        return parameters;
    }

    public Type getReturnType() {
        return returnType;
    }

    public Statement getBody() {
        return body;
    }

    @Override
    public Type getReturnType(ArrayList<Expression> arguments) {
        return returnType;
    }

    @Override
    public Object execute(ArrayList<Expression> arguments, Program program) {
        ArrayList<Object> values = new ArrayList<>();
        for (Expression argument : arguments) {
            values.add(argument.calculate(program));
        }
        program.pushAllToStack(values);
        body.run(program);
        for (UserFunctionParameter ignored : parameters) {
            program.popFromStack();
        }
        return program.getReturnValueAndResume();
    }

    @Override
    public ValidationResult validate(ArrayList<Expression> arguments, ParserRuleContext ctx) {
        return validateArguments(arguments, ctx, argumentTypes);
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(argumentTypes.length);
    }

    public static class UserFunctionParameter {

        private final int varId;
        private final Type type;

        public UserFunctionParameter(Variable variable) {
            assert variable.isOnStack();
            this.varId = variable.getId();
            this.type = variable.getType();
        }

        public int getVarId() {
            return varId;
        }

        public Type getType() {
            return type;
        }
    }
}
