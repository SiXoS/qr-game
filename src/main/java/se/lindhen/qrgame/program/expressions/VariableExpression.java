package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.Variable;

public class VariableExpression extends Expression {

    private final int varId;

    public VariableExpression(Variable variable) {
        super(variable.getType());
        assert !variable.isOnStack();
        this.varId = variable.getId();
    }

    @Override
    public Object calculate(Program program) {
        return program.getVariable(varId, false);
    }

    public int getVarId() {
        return varId;
    }
}
