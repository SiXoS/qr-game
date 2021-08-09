package se.lindhen.qrgame.program.statements;

import se.lindhen.qrgame.program.Interrupt;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.objects.ObjectValue;

import java.util.Iterator;

public class ForEachStatement extends Statement {

    private final Expression toIterate;
    private final int targetVarId;
    private final Statement body;
    private final Integer label;

    public ForEachStatement(Expression toIterate, int targetVarId, Statement body, Integer label) {
        this.toIterate = toIterate;
        this.targetVarId = targetVarId;
        this.body = body;
        this.label = label;
    }

    @Override
    public void runInternal(Program program) {
        Iterator<Object> iterator = ((ObjectValue) toIterate.calculate(program)).iterator();
        while (iterator.hasNext()) {
            program.setVariable(targetVarId, iterator.next());
            body.run(program);
            Interrupt interrupt = program.catchInterrupt(label);
            if (interrupt != null) {
                if (interrupt.isBreakElseContinue())
                    break;
            } else if (!program.isRunning()) {
                break;
            }
        }
    }

    @Override
    public boolean alwaysReturns() {
        return false;
    }

    public Expression getToIterate() {
        return toIterate;
    }

    public int getTargetVarId() {
        return targetVarId;
    }

    public Statement getBody() {
        return body;
    }

    public Integer getLabel() {
        return label;
    }

    public boolean hasLabel() {
        return label != null;
    }
}
