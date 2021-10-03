package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.functions.LegacyFunction;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.objects.IndexedHashSetClass;
import se.lindhen.qrgame.util.IndexedHashSet;
import se.lindhen.qrgame.program.objects.utils.CollectionConstructionUtils;

import java.util.List;
import java.util.Optional;

public class IndexedHashSetLegacyFunction extends Function implements LegacyFunction {

    public static final String NAME = "indexedHashSet";

    public IndexedHashSetLegacyFunction() {
        super(NAME, null);
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        IndexedHashSet<Object> set = CollectionConstructionUtils.createCollection(arguments, program, size -> new IndexedHashSet<>());
        return IndexedHashSetClass.getQgClass().createInstance(set);
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
        return IndexedHashSetClass.getQgClass().getObjectType(arguments);
    }
}
