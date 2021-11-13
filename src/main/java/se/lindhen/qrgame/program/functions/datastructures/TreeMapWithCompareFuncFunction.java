package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.objects.TreeMapClass;
import se.lindhen.qrgame.program.types.*;

import java.util.*;

public class TreeMapWithCompareFuncFunction extends Function {

    public TreeMapWithCompareFuncFunction() {
        super("treeMap", new FunctionType(
                TreeMapClass.getQgClass().getObjectTypeFromTypeArgs(Arrays.asList(new GenericType(0), new GenericType(1))),
                new FunctionType(NumberType.NUMBER_TYPE, new GenericType(0), new GenericType(0)),
                new TypeType(new GenericType(1))));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        Function sortableFunction = (Function) arguments.get(0).calculate(program);
        return TreeMapClass.getQgClass().createInstance(new TreeMap<>(new FunctionReferenceComparator(sortableFunction, program)));
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
