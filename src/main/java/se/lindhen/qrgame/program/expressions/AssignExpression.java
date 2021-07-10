package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;

public class AssignExpression extends Expression {

    private final int assignTo;
    private final Expression expression;

    public AssignExpression(int assignTo, Expression expression) {
        super(expression.getType(), expression);
        this.assignTo = assignTo;
        this.expression = expression;
    }

    public int getAssignTo() {
        return assignTo;
    }

    @Override
    public Object calculate(Program program) {
        Object value = expression.calculate(program);
        program.setVariable(assignTo, value);
        return value;
    }
}
