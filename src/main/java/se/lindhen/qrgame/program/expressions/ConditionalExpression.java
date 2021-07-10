package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;

public class ConditionalExpression extends Expression {
    private final Expression condition;
    private final Expression trueCase;
    private final Expression falseCase;

    public ConditionalExpression(Expression condition, Expression trueCase, Expression falseCase) {
        super(trueCase.getType());
        this.condition = condition;
        this.trueCase = trueCase;
        this.falseCase = falseCase;
    }

    @Override
    public Object calculate(Program program) {
        return (boolean) condition.calculate(program) ? trueCase.calculate(program) : falseCase.calculate(program);
    }

    public Expression getCondition() {
        return condition;
    }

    public Expression getTrueCase() {
        return trueCase;
    }

    public Expression getFalseCase() {
        return falseCase;
    }
}
