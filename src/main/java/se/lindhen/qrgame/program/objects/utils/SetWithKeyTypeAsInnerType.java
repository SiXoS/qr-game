package se.lindhen.qrgame.program.objects.utils;

import se.lindhen.qrgame.program.objects.GenericType;
import se.lindhen.qrgame.program.objects.QgClass;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;

import java.util.Collections;

public class SetWithKeyTypeAsInnerType implements GenericType {

    private final QgClass<?> setClass;

    public SetWithKeyTypeAsInnerType(QgClass<?> setClass) {
        this.setClass = setClass;
    }

    @Override
    public Type getType(ObjectType objectType) {
        return setClass.getObjectTypeFromTypeArgs(Collections.singletonList(objectType.getInnerTypes().get(0)));
    }
}
