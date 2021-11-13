package se.lindhen.qrgame.program.functions.datastructures;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.ConstantExpression;
import se.lindhen.qrgame.program.functions.Function;

import java.util.Arrays;
import java.util.Comparator;

public class FunctionReferenceComparator implements Comparator<Object> {

    private final Function compareFunction;
    private final Program program;

    public FunctionReferenceComparator(Function compareFunction, Program program) {
        this.compareFunction = compareFunction;
        this.program = program;
    }

    @Override
    public int compare(Object o1, Object o2) {
        double comparison = (double) compareFunction.execute(Arrays.asList(new ConstantExpression(o1), new ConstantExpression(o2)), program);
        return Double.compare(comparison, 0);
    }
}
