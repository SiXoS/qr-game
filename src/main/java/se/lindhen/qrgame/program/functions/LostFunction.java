package se.lindhen.qrgame.program.functions;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.GameStatus;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.BoolType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.VoidType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LostFunction extends Function {

    private static final String NAME = "lost";

    public LostFunction() {
        super(NAME, new FunctionDeclaration(0, VoidType.VOID_TYPE, BoolType.BOOL_TYPE));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        program.setStatus(GameStatus.LOST);
        program.setTrackScore((boolean) arguments.get(0).calculate(program));
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
