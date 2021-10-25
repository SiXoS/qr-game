package se.lindhen.qrgame.program.functions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.VoidType;

import java.util.List;
import java.util.Optional;

public class SetScoreFunction extends Function {

    private final static String NAME = "setScore";

    public SetScoreFunction() {
        super(NAME, new FunctionType(VoidType.VOID_TYPE, NumberType.NUMBER_TYPE));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        program.setScore((int)(double)arguments.get(0).calculate(program));
        return null;
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
