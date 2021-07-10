package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.Variable;

public class StackVariableExpression extends Expression {

    private final int varId;

    public StackVariableExpression(Variable variable) {
        super(variable.getType());
        assert variable.isOnStack();
        this.varId = variable.getId();
    }

    @Override
    public Object calculate(Program program) {
        return program.getFromStack(varId);
    }

    public int getVarId() {
        return varId;
    }
}
