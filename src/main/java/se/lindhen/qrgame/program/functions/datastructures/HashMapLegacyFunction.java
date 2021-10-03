package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.functions.LegacyFunction;
import se.lindhen.qrgame.program.objects.HashMapClass;
import se.lindhen.qrgame.program.objects.utils.CollectionConstructionUtils;
import se.lindhen.qrgame.program.objects.utils.MapConstructionUtils;
import se.lindhen.qrgame.program.types.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class HashMapLegacyFunction extends Function implements LegacyFunction {

    private static final String NAME = "hashMap";

    public HashMapLegacyFunction() {
        super(NAME, null);
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        HashMap<Object, Object> map = MapConstructionUtils.createMap(arguments, program, HashMap::new);
        return HashMapClass.getQgClass().createInstance(map);
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
        return MapConstructionUtils.typeFromArguments(HashMapClass.getQgClass(), arguments);
    }
}
