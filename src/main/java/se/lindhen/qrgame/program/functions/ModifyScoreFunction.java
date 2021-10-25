package se.lindhen.qrgame.program.functions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.List;
import java.util.Optional;

public class ModifyScoreFunction extends Function {

    private static final String NAME = "modifyScore";

    public ModifyScoreFunction() {
        super(NAME, new FunctionType(NumberType.NUMBER_TYPE, NumberType.NUMBER_TYPE));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        program.setScore(program.getScore() + (int)(double) arguments.get(0).calculate(program));
        return (double) program.getScore();
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
