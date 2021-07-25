package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.types.TypeType;

public class TypeExpression extends Expression {

    private final Type type;

    public TypeExpression(Type type) {
        super(new TypeType(type));
        this.type = type;
    }

    public Type getTypeValue() {
        return type;
    }

    @Override
    public Object calculate(Program program) {
        return type;
    }
}
