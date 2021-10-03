package se.lindhen.qrgame.program.functions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.NumberType;

import java.util.List;
import java.util.Optional;

public class TimeDiffFunction extends Function {

    public static final String NAME = "timeDiff";

    public TimeDiffFunction() {
        super(NAME, new FunctionDeclaration(0, NumberType.NUMBER_TYPE));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        return program.getSecondsDeltaTime();
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(0);
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
