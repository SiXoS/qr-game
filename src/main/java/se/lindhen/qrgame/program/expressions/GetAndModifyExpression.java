package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.NumberType;

public class GetAndModifyExpression extends Expression {

    private final int varId;
    private final boolean onStack;
    private final boolean incrementElseDecrement;

    public GetAndModifyExpression(int varId, boolean onStack, boolean incrementElseDecrement) {
        super(NumberType.NUMBER_TYPE);
        this.varId = varId;
        this.onStack = onStack;
        this.incrementElseDecrement = incrementElseDecrement;
    }

    public int getVarId() {
        return varId;
    }

    public boolean isIncrementElseDecrement() {
        return incrementElseDecrement;
    }

    public boolean isOnStack() {
        return onStack;
    }

    @Override
    public Object calculate(Program program) {
        Object before = program.getVariable(varId, onStack);
        double value = (double) before + (incrementElseDecrement ? 1 : -1);
        if (onStack) {
            program.setStackVariable(varId, value);
        } else {
            program.setVariable(varId, value);
        }
        return before;
    }
}
