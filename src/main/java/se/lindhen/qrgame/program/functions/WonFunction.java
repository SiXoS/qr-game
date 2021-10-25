package se.lindhen.qrgame.program.functions;

import se.lindhen.qrgame.program.GameStatus;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.program.types.VoidType;

import java.util.List;
import java.util.Optional;

public class WonFunction extends Function {

    private static final String NAME = "won";

    public WonFunction() {
        super(NAME, new FunctionType(VoidType.VOID_TYPE));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        program.setStatus(GameStatus.WON);
        program.setTrackScore(true);
        return null;
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
