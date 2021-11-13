package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;

public class ConstantExpression extends Expression {

    private final Object value;

    public ConstantExpression(Object value) {
        super(null);
        this.value = value;
    }

    @Override
    public Object calculate(Program program) {
        return value;
    }
}
