package se.lindhen.qrgame.program.objects.utils;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.Method;
import se.lindhen.qrgame.program.objects.ObjectValue;
import se.lindhen.qrgame.program.types.*;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;

public class ForEachMethod<O extends ObjectValue> extends Method<O> {

    private final BiConsumer<O, Object> modifier;

    public ForEachMethod(String name, BiConsumer<O, Object> modifier, ObjectType typeOfThisObject) {
        this(name, modifier, typeOfThisObject, 0);
    }

    public ForEachMethod(String name, BiConsumer<O, Object> modifier, ObjectType typeOfThisObject, int genericIdInSource) {
        super(name, new FunctionType(
                VoidType.VOID_TYPE,
                typeOfThisObject,
                new IterableType(new GenericType(genericIdInSource))
        ));
        this.modifier = modifier;
    }

    @Override
    public Object execute(O object, List<Expression> arguments, Program program) {
        ObjectValue toIterate = (ObjectValue) arguments.get(0).calculate(program);
        Iterator<Object> iterator = toIterate.iterator();
        while (iterator.hasNext()) {
            modifier.accept(object, iterator.next());
        }
        return null;
    }

}
