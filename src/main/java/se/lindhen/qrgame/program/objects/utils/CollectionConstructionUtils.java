package se.lindhen.qrgame.program.objects.utils;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.TypeType;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class CollectionConstructionUtils {

    private CollectionConstructionUtils() {}

    public static Type innerTypeFromArguments(List<Type> arguments) {
        Type typeOfFirstArg = arguments.get(0);
        return typeOfFirstArg.isType() ? ((TypeType) typeOfFirstArg).getActualType() : typeOfFirstArg;
    }

    public static <T extends Collection<Object>> T createCollection(List<Expression> arguments, Program program, Function<Integer, T> collectionCreator) {
        T backingCollection;
        if (arguments.get(0).getType().isType()) {
            if (arguments.size() == 2) {
                backingCollection = collectionCreator.apply((int)(double)arguments.get(1).calculate(program));
            } else {
                backingCollection = collectionCreator.apply(10);
            }
        } else {
            backingCollection = collectionCreator.apply(arguments.size());
            for (Expression argument : arguments) {
                backingCollection.add(argument.calculate(program));
            }
        }
        return backingCollection;
    }

}
