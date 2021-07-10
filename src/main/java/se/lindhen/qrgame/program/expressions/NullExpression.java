package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.Type;

public class NullExpression extends Expression {

    public NullExpression(Type type) {
        super(type);
    }

    @Override
    public Object calculate(Program program) {
        return null;
    }
}
