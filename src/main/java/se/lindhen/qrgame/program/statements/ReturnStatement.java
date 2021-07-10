package se.lindhen.qrgame.program.statements;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;

public class ReturnStatement extends Statement {

    private final Expression value;

    public ReturnStatement(Expression value) {
        this.value = value;
    }

    public ReturnStatement() {
        this(null);
    }

    public Expression getValue() {
        return value;
    }

    public boolean hasValue() {
        return value != null;
    }

    @Override
    void runInternal(Program program) {
        program.setReturnValueAndExitFunction(value == null ? null : value.calculate(program));
    }

    @Override
    public boolean alwaysReturns() {
        return true;
    }
}
