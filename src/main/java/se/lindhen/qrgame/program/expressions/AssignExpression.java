package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;

public class AssignExpression extends Expression {

    private final int assignTo;
    private final boolean onStack;
    private final Expression expression;

    public AssignExpression(int assignTo, boolean onStack, Expression expression) {
        super(expression.getType(), expression);
        this.assignTo = assignTo;
        this.onStack = onStack;
        this.expression = expression;
    }

    public int getAssignTo() {
        return assignTo;
    }

    public boolean isOnStack() {
        return onStack;
    }

    @Override
    public Object calculate(Program program) {
        Object value = expression.calculate(program);
        if (onStack) {
            program.setStackVariable(assignTo, value);
        } else {
            program.setVariable(assignTo, value);
        }
        return value;
    }
}
