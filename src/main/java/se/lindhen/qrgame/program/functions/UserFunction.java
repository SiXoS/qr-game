package se.lindhen.qrgame.program.functions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.Variable;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.statements.Statement;
import se.lindhen.qrgame.program.types.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserFunction extends Function {

    private final int id;
    private final int numberOfTrueArguments;
    private final Optional<Integer> constantArgumentCount;
    private final Statement body;

    public UserFunction(int id, FunctionDeclaration functionDeclaration, Statement body) {
        super("_user_function_" + id, functionDeclaration);
        this.id = id;
        numberOfTrueArguments = functionDeclaration.numberOfTrueArguments();
        constantArgumentCount = functionDeclaration.getConstantParameterCount();
        this.body = body;
    }

    public UserFunction(int id, int numberOfTrueArguments, Optional<Integer> constantArgumentCount, Statement body) {
        super("_user_function_" + id, null);
        this.id = id;
        this.numberOfTrueArguments = numberOfTrueArguments;
        this.constantArgumentCount = constantArgumentCount;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public Statement getBody() {
        return body;
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        ArrayList<Object> values = new ArrayList<>();
        for (Expression argument : arguments) {
            values.add(argument.calculate(program));
        }
        program.pushAllToStack(values);
        body.run(program);
        for (int i = 0; i < numberOfTrueArguments; i++) {
            program.popFromStack();
        }
        return program.getReturnValueAndResume();
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return constantArgumentCount;
    }

    public int getNumberOfTrueArguments() {
        return numberOfTrueArguments;
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
