package se.lindhen.qrgame.program.functions.math;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.functions.FunctionDeclaration;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.types.VarargType;

import java.util.List;
import java.util.Optional;

public class MaxFunction extends Function {
    public MaxFunction() {
        super("max", new FunctionDeclaration(0, NumberType.NUMBER_TYPE, NumberType.NUMBER_TYPE, new VarargType(NumberType.NUMBER_TYPE)));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        double max = Double.MIN_VALUE;
        for (Expression argument : arguments) {
            max = Math.max(max, (double) argument.calculate(program));
        }
        return max;
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
