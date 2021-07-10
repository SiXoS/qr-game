package se.lindhen.qrgame.program.objects.utils;

import se.lindhen.qrgame.program.objects.GenericType;
import se.lindhen.qrgame.program.objects.ListClass;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;

import java.util.Collections;

public class ListWithValueTypeAsInnerType implements GenericType {
    @Override
    public Type getType(ObjectType objectType) {
        return ListClass.getQgClass().getObjectTypeFromTypeArgs(Collections.singletonList(objectType.getInnerTypes().get(1)));
    }
}
