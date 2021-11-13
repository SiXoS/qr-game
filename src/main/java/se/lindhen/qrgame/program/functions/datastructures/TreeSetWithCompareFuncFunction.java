package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.ConstantExpression;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.objects.TreeSetClass;
import se.lindhen.qrgame.program.types.*;

import java.util.*;

public class TreeSetWithCompareFuncFunction extends Function {

    public TreeSetWithCompareFuncFunction() {
        super("treeSet", new FunctionType(
                TreeSetClass.getQgClass().getObjectTypeFromTypeArgs(Collections.singletonList(new GenericType(0))),
                new FunctionType(NumberType.NUMBER_TYPE, new GenericType(0), new GenericType(0))));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        Function sortableFunction = (Function) arguments.get(0).calculate(program);
        return TreeSetClass.getQgClass().createInstance(new TreeSet<>(new FunctionReferenceComparator(sortableFunction, program)));
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(1);
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
