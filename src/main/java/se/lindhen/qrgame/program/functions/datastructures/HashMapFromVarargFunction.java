package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.functions.FunctionDeclaration;
import se.lindhen.qrgame.program.objects.HashMapClass;
import se.lindhen.qrgame.program.objects.MapEntryClass;
import se.lindhen.qrgame.program.types.GenericType;
import se.lindhen.qrgame.program.types.TypeType;
import se.lindhen.qrgame.program.types.VarargType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class HashMapFromVarargFunction extends Function {

    public HashMapFromVarargFunction() {
        super("hashMap", new FunctionDeclaration(2,
                HashMapClass.getQgClass().getObjectTypeFromTypeArgs(Arrays.asList(new GenericType(0), new GenericType(1))),
                new VarargType(MapEntryClass.getQgClass().getObjectTypeFromTypeArgs(Arrays.asList(new GenericType(0), new GenericType(1))))));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        HashMap<Object, Object> hashMap = new HashMap<>();
        for (Expression argument : arguments) {
            MapEntryClass.MapEntryObject mapEntry = (MapEntryClass.MapEntryObject) argument.calculate(program);
            hashMap.put(mapEntry.getKey(), mapEntry.getValue());
        }
        return HashMapClass.getQgClass().createInstance(hashMap);
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
