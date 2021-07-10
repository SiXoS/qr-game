package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.BoolType;

public class NotExpression extends Expression {

    private final Expression inner;

    public NotExpression(Expression inner) {
        super(BoolType.BOOL_TYPE, inner);
        this.inner = inner;
    }

    @Override
    public Object calculate(Program program) {
        return !(boolean) inner.calculate(program);
    }
}
