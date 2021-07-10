package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.BoolType;
import se.lindhen.qrgame.program.types.Type;

public class BoolExpression extends Expression {

    private final boolean bool;

    public BoolExpression(boolean bool) {
        super(BoolType.BOOL_TYPE);
        this.bool = bool;
    }

    @Override
    public Object calculate(Program program) {
        return bool;
    }

    public boolean getBool() {
        return bool;
    }
}
