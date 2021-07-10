package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.BoolType;

import java.util.Objects;

public class EqualsExpression extends Expression {

    private final Expression lhs;
    private final Expression rhs;

    public EqualsExpression(Expression lhs, Expression rhs) {
        super(BoolType.BOOL_TYPE, lhs, rhs);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public Object calculate(Program program) {
        return Objects.equals(lhs.calculate(program), rhs.calculate(program));
    }
}
