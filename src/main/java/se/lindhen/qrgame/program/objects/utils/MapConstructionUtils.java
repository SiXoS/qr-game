package se.lindhen.qrgame.program.objects.utils;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.MapEntryClass;
import se.lindhen.qrgame.program.objects.QgClass;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.TypeType;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class MapConstructionUtils {

    public static ObjectType typeFromArguments(QgClass<?> parent, List<Type> arguments) {
        return arguments.get(0).isType() ?
                new ObjectType(parent, ((TypeType) arguments.get(0)).getActualType(), ((TypeType) arguments.get(1)).getActualType()) :
                new ObjectType(parent, ((ObjectType) arguments.get(0)).getInnerTypes().get(0), ((ObjectType) arguments.get(0)).getInnerTypes().get(1));
    }

    public static <T extends Map<Object, Object>> T createMap(List<Expression> arguments, Program program, Supplier<T> mapCreator) {
        T map = mapCreator.get();
        if (!arguments.get(0).getType().isType()) {
            for (Expression argument : arguments) {
                MapEntryClass.MapEntryObject entry = (MapEntryClass.MapEntryObject) argument.calculate(program);
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

}
