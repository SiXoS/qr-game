package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.NumberType;

public class NegateExpression extends Expression {

    private final Expression inner;

    public NegateExpression(Expression inner) {
        super(NumberType.NUMBER_TYPE, inner);
        this.inner = inner;
    }

    @Override
    public Object calculate(Program program) {
        return -(double) inner.calculate(program);
    }
}
