package se.lindhen.qrgame.program.objects.utils;

import se.lindhen.qrgame.program.objects.GenericType;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;

public class ConstantGenericType implements GenericType {

    private final Type type;

    public ConstantGenericType(Type type) {
        this.type = type;
    }

    @Override
    public Type getType(ObjectType objectType) {
        return type;
    }
}
