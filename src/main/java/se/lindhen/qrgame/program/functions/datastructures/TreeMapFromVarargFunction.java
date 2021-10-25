package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.program.objects.MapEntryClass;
import se.lindhen.qrgame.program.objects.TreeMapClass;
import se.lindhen.qrgame.program.types.GenericType;
import se.lindhen.qrgame.program.types.VarargType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

public class TreeMapFromVarargFunction extends Function {

    public TreeMapFromVarargFunction() {
        super("treeMap", new FunctionType(
                TreeMapClass.getQgClass().getObjectTypeFromTypeArgs(Arrays.asList(new GenericType(0), new GenericType(1))),
                new VarargType(MapEntryClass.getQgClass().getObjectTypeFromTypeArgs(Arrays.asList(new GenericType(0), new GenericType(1))))));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        TreeMap<Object, Object> treeMap = new TreeMap<>();
        for (Expression argument : arguments) {
            MapEntryClass.MapEntryObject mapEntry = (MapEntryClass.MapEntryObject) argument.calculate(program);
            treeMap.put(mapEntry.getKey(), mapEntry.getValue());
        }
        return TreeMapClass.getQgClass().createInstance(treeMap);
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.empty();
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
