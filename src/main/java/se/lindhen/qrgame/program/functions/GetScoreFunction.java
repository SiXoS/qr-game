package se.lindhen.qrgame.program.functions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.List;
import java.util.Optional;

public class GetScoreFunction extends Function {

    private static final String NAME = "getScore";

    public GetScoreFunction() {
        super(NAME, new FunctionDeclaration(0, NumberType.NUMBER_TYPE));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        return (double) program.getScore();
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
