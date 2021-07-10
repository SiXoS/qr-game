package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.BoolType;

public class OrExpression extends Expression {

    private final Expression lhs;
    private final Expression rhs;

    public OrExpression(Expression lhs, Expression rhs) {
        super(BoolType.BOOL_TYPE, lhs, rhs);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Object calculate(Program program) {
        return (boolean) lhs.calculate(program) || (boolean) rhs.calculate(program);
    }

}
