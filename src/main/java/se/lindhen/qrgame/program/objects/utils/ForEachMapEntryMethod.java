package se.lindhen.qrgame.program.objects.utils;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.MapEntryClass;
import se.lindhen.qrgame.program.objects.Method;
import se.lindhen.qrgame.program.objects.ObjectValue;
import se.lindhen.qrgame.program.types.*;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.util.TriConsumer;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ForEachMapEntryMethod<O extends ObjectValue> extends Method<O> {

    private final TriConsumer<O, Object, Object> modifier;

    public ForEachMapEntryMethod(String name, TriConsumer<O, Object, Object> objectModifier, Type typeOfThisObject) {
        super(name, new FunctionType(
                VoidType.VOID_TYPE,
                typeOfThisObject,
                new IterableType(MapEntryClass.getQgClass().getObjectTypeFromTypeArgs(Arrays.asList(new GenericType(0), new GenericType(1))))));
        this.modifier = objectModifier;
    }

    @Override
    public Object execute(O object, List<Expression> arguments, Program program) {
        ObjectValue toIterate = (ObjectValue) arguments.get(0).calculate(program);
        Iterator<Object> iterator = toIterate.iterator();
        while (iterator.hasNext()) {
            MapEntryClass.MapEntryObject entry = (MapEntryClass.MapEntryObject) iterator.next();
            modifier.apply(object, entry.getKey(), entry.getValue());
        }
        return null;
    }
}
