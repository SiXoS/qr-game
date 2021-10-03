package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.functions.LegacyFunction;
import se.lindhen.qrgame.program.objects.HashSetClass;
import se.lindhen.qrgame.program.objects.utils.CollectionConstructionUtils;
import se.lindhen.qrgame.program.types.Type;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class HashSetLegacyFunction extends Function implements LegacyFunction {

    public static final String NAME = "hashSet";

    public HashSetLegacyFunction() {
        super(NAME, null);
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        HashSet<Object> set = CollectionConstructionUtils.createCollection(arguments, program, HashSet::new);
        return HashSetClass.getQgClass().createInstance(set);
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.empty();
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public Type getReturnType(List<Type> arguments) {
        return HashSetClass.getQgClass().getObjectType(arguments);
    }
}
