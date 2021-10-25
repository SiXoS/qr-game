package se.lindhen.qrgame.program.functions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandFunction extends Function {

    public static final String NAME = "rand";
    private final Random random = new Random();

    public RandFunction() {
        super(NAME, new FunctionType(NumberType.NUMBER_TYPE, NumberType.NUMBER_TYPE, NumberType.NUMBER_TYPE));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        double minNum = (double) arguments.get(0).calculate(program);
        double maxNum = (double) arguments.get(1).calculate(program);
        return random.nextDouble()*(maxNum - minNum) + minNum;
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(2);
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
