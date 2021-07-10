package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.Type;

public class GetAndModifyExpression extends Expression {

    private final int varId;
    private final boolean incrementElseDecrement;

    public GetAndModifyExpression(int varId, boolean incrementElseDecrement) {
        super(NumberType.NUMBER_TYPE);
        this.varId = varId;
        this.incrementElseDecrement = incrementElseDecrement;
    }

    public int getVarId() {
        return varId;
    }

    public boolean isIncrementElseDecrement() {
        return incrementElseDecrement;
    }

    @Override
    public Object calculate(Program program) {
        Object before = program.getVariable(varId);
        program.setVariable(varId, (double) before + (incrementElseDecrement ? 1 : -1));
        return before;
    }
}
