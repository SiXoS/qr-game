package se.lindhen.qrgame.program.statements;

import se.lindhen.qrgame.program.Program;

public abstract class Statement {

    public final void run(Program program) {
        if (!program.isRunning()) return;
        runInternal(program);
    }

    abstract void runInternal(Program program);

    public abstract boolean alwaysReturns();

}
