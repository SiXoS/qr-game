package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.functions.LegacyFunction;
import se.lindhen.qrgame.program.objects.TreeSetClass;
import se.lindhen.qrgame.program.objects.utils.CollectionConstructionUtils;
import se.lindhen.qrgame.program.types.Type;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public class TreeSetLegacyFunction extends Function implements LegacyFunction {

    public static final String NAME = "treeSet";

    public TreeSetLegacyFunction() {
        super(NAME, null);
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        TreeSet<Object> set = CollectionConstructionUtils.createCollection(arguments, program, size -> new TreeSet<>());
        return TreeSetClass.getQgClass().createInstance(set);
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
        return TreeSetClass.getQgClass().getObjectType(arguments);
    }
}
