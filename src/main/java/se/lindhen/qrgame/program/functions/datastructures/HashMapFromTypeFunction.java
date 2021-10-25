package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.program.objects.HashMapClass;
import se.lindhen.qrgame.program.types.GenericType;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.TypeType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class HashMapFromTypeFunction extends Function {

    public HashMapFromTypeFunction() {
        super("hashMap", new FunctionType(
                HashMapClass.getQgClass().getObjectTypeFromTypeArgs(Arrays.asList(new GenericType(0), new GenericType(1))),
                new TypeType(new GenericType(0)),
                new TypeType(new GenericType(1))));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        return HashMapClass.getQgClass().createInstance(new HashMap<>());
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(2);
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
