package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.NumberType;

public class DivideExpression extends Expression {

    private final Expression lhs;
    private final Expression rhs;

    public DivideExpression(Expression lhs, Expression rhs) {
        super(NumberType.NUMBER_TYPE, lhs, rhs);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Object calculate(Program program) {
        return (double) lhs.calculate(program) / (double) rhs.calculate(program);
    }
}
