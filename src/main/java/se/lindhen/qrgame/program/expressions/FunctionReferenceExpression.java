package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.FunctionType;

public class FunctionReferenceExpression extends Expression {

    private final int functionId;
    private final boolean isUserFunction;

    public FunctionReferenceExpression(int functionId, boolean isUserFunction, FunctionType functionType) {
        super(functionType);
        this.functionId = functionId;
        this.isUserFunction = isUserFunction;
    }

    @Override
    public Object calculate(Program program) {
        return isUserFunction ? program.getUserFunction(functionId) : program.getFunction(functionId);
    }

    public int getFunctionId() {
        return functionId;
    }

    public boolean isUserFunction() {
        return isUserFunction;
    }
}
