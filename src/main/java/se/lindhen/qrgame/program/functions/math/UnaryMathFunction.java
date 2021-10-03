package se.lindhen.qrgame.program.functions.math;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.functions.FunctionDeclaration;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;

import java.util.List;
import java.util.Optional;

public abstract class UnaryMathFunction extends Function {

    public UnaryMathFunction(String name) {
        super(name, new FunctionDeclaration(0, NumberType.NUMBER_TYPE, NumberType.NUMBER_TYPE));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        return calculate((double) arguments.get(0).calculate(program));
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(1);
    }

    public abstract double calculate(double arg);

    @Override
    public boolean isConstant() {
        return true;
    }
}
