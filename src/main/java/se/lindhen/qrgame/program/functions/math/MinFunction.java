package se.lindhen.qrgame.program.functions.math;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.types.VarargType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MinFunction extends Function {

    public MinFunction() {
        super("min", new FunctionType(NumberType.NUMBER_TYPE, NumberType.NUMBER_TYPE, new VarargType(NumberType.NUMBER_TYPE)));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        double min = Double.MAX_VALUE;
        for (Expression argument : arguments) {
            min = Math.min(min, (double) argument.calculate(program));
        }
        return min;
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
