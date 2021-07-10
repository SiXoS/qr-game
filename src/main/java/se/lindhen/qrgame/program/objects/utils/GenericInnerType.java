package se.lindhen.qrgame.program.objects.utils;

import se.lindhen.qrgame.program.objects.GenericType;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;

public class GenericInnerType implements GenericType {

    private final int innerTypeIndex;

    public GenericInnerType(int innerTypeIndex) {
        this.innerTypeIndex = innerTypeIndex;
    }

    public GenericInnerType() {
        this.innerTypeIndex = 0;
    }

    @Override
    public Type getType(ObjectType objectType) {
        return objectType.getInnerTypes().get(innerTypeIndex);
    }
}
