package se.lindhen.qrgame.program.functions.math;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;

import java.util.List;
import java.util.Optional;

public abstract class NullaryMathFunction extends Function {

    public NullaryMathFunction(String name) {
        super(name, new FunctionType(NumberType.NUMBER_TYPE));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        return calculate();
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(0);
    }

    public abstract double calculate();

    @Override
    public boolean isConstant() {
        return true;
    }
}
