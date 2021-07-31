package se.lindhen.qrgame.program.functions;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.ArrayList;
import java.util.Optional;

public class TimeFunction extends Function {

    public static final String NAME = "time";

    public TimeFunction() {
        super(NAME);
    }

    @Override
    public Type getReturnType(ArrayList<Expression> arguments) {
        return NumberType.NUMBER_TYPE;
    }

    @Override
    public Object execute(ArrayList<Expression> arguments, Program program) {
        return program.getSecondsSinceStart();
    }

    @Override
    public ValidationResult validate(ArrayList<Expression> arguments, ParserRuleContext ctx) {
        return validateArguments(arguments, ctx);
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
