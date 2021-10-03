package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.functions.LegacyFunction;
import se.lindhen.qrgame.program.objects.utils.CollectionConstructionUtils;
import se.lindhen.qrgame.program.objects.utils.MapConstructionUtils;
import se.lindhen.qrgame.program.objects.TreeMapClass;
import se.lindhen.qrgame.program.types.Type;

import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

public class TreeMapLegacyFunction extends Function implements LegacyFunction {

    private static final String NAME = "treeMap";

    public TreeMapLegacyFunction() {
        super(NAME, null);
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        TreeMap<Object, Object> map = MapConstructionUtils.createMap(arguments, program, TreeMap::new);
        return TreeMapClass.getQgClass().createInstance(map);
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
        return MapConstructionUtils.typeFromArguments(TreeMapClass.getQgClass(), arguments);
    }
}
