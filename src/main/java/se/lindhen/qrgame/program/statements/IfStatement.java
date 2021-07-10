package se.lindhen.qrgame.program.statements;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;

public class IfStatement extends Statement {

    private final Expression condition;
    private final Statement body;
    private Statement elseBody;

    public IfStatement(Expression condition, Statement trueBody) {
        this.condition = condition;
        this.body = trueBody;
    }

    public void addFalseBody(Statement falseBody) {
        this.elseBody = falseBody;
    }

    @Override
    public void runInternal(Program program) {
        if ((boolean) condition.calculate(program)) {
            body.run(program);
        } else if (elseBody != null) {
            elseBody.run(program);
        }
    }

    @Override
    public boolean alwaysReturns() {
        return elseBody != null && body.alwaysReturns() && elseBody.alwaysReturns();
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getBody() {
        return body;
    }

    public Statement getElseBody() {
        return elseBody;
    }

    public boolean hasElseBody() {
        return elseBody != null;
    }

}
