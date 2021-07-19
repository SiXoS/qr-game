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
import java.util.Optional;

public class LostFunction extends Function {

    private static final String NAME = "lost";

    public LostFunction() {
        super(NAME);
    }

    @Override
    public Type getReturnType(ArrayList<Expression> arguments) {
        return VoidType.VOID_TYPE;
    }

    @Override
    public Object execute(ArrayList<Expression> arguments, Program program) {
        program.setStatus(GameStatus.LOST);
        program.setTrackScore((boolean) arguments.get(0).calculate(program));
        return null;
    }

    @Override
    public ValidationResult validate(ArrayList<Expression> arguments, ParserRuleContext ctx) {
        return validateArguments(arguments, ctx, BoolType.BOOL_TYPE);
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
