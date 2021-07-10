package se.lindhen.qrgame.program.statements;

import se.lindhen.qrgame.program.Interrupt;
import se.lindhen.qrgame.program.Program;

public class InterruptStatement extends Statement {

    private final boolean breakElseContinue;
    private final Integer label;

    public InterruptStatement(boolean breakElseContinue, Integer label) {
        this.breakElseContinue = breakElseContinue;
        this.label = label;
    }

    @Override
    void runInternal(Program program) {
        program.interruptLoop(new Interrupt(label, breakElseContinue));
    }

    @Override
    public boolean alwaysReturns() {
        return false;
    }

    public boolean isBreakElseContinue() {
        return breakElseContinue;
    }

    public Integer getLabel() {
        return label;
    }

    public boolean hasLabel() {
        return label != null;
    }
}
