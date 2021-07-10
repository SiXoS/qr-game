package se.lindhen.qrgame.program.statements;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;

public class ExpressionStatement extends Statement {

    private final Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void runInternal(Program program) {
        expression.calculate(program);
    }

    @Override
    public boolean alwaysReturns() {
        return false;
    }

    public Expression getExpression() {
        return expression;
    }

}
