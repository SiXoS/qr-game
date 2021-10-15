package se.lindhen.qrgame.program.objects.utils;

import se.lindhen.qrgame.program.objects.ListClass;
import se.lindhen.qrgame.program.objects.QgClass;
import se.lindhen.qrgame.program.types.GenericType;
import se.lindhen.qrgame.program.types.Type;

import java.util.Collections;

public class TypeUtils {

    private TypeUtils(){}

    public static Type listWithGenericType(int genericId) {
        return ListClass.getQgClass().getObjectTypeFromTypeArgs(Collections.singletonList(new GenericType(genericId)));
    }

    public static Type setWithGenericType(QgClass<?> qgClass, int genericId) {
        return qgClass.getObjectTypeFromTypeArgs(Collections.singletonList(new GenericType(genericId)));
    }

}
