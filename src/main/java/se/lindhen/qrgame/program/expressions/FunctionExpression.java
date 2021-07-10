package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.Type;

import java.util.ArrayList;

public class FunctionExpression extends Expression {

    private final ArrayList<Expression> arguments;
    private final boolean userFunction;
    private final int functionId;

    public FunctionExpression(int functionId, Type returnType, ArrayList<Expression> arguments, boolean userFunction) {
        super(returnType, arguments);
        this.functionId = functionId;
        this.arguments = arguments;
        this.userFunction = userFunction;
    }

    @Override
    public Object calculate(Program program) {
        if (userFunction) {
            return program.getUserFunction(functionId).execute(arguments, program);
        } else {
            return program.getFunction(functionId).execute(arguments, program);
        }
    }

    public int getFunctionId() {
        return functionId;
    }

    public boolean isUserFunction() {
        return userFunction;
    }
}
