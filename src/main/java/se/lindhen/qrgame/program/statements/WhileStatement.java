package se.lindhen.qrgame.program.statements;

import se.lindhen.qrgame.program.Interrupt;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;

public class WhileStatement extends Statement {

    private final Expression condition;
    private final Statement body;
    private final Statement postBody; // necessary for for-i loops
    private final Integer label;

    public WhileStatement(Expression condition, Statement body, Integer label) {
        this(condition, body, null, label);
    }

    public WhileStatement(Expression condition, Statement body, Statement postBody, Integer label) {
        this.condition = condition;
        this.body = body;
        this.postBody = postBody;
        this.label = label;
    }

    @Override
    public void runInternal(Program program) {
        while ((boolean) condition.calculate(program)) {
            body.run(program);
            Interrupt interrupt = program.catchInterrupt(label);
            if (interrupt != null) {
                if (interrupt.isBreakElseContinue())
                    break;
            } else if (!program.isRunning()) {
                break;
            }
            if (hasPostBody())
                postBody.runInternal(program);
        }
    }

    @Override
    public boolean alwaysReturns() {
        return false;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getBody() {
        return body;
    }

    public Statement getPostBody() {
        return postBody;
    }

    public Integer getLabel() {
        return label;
    }

    public boolean hasPostBody() {
        return postBody != null;
    }

    public boolean hasLabel() {
        return label != null;
    }
}
