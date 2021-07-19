package se.lindhen.qrgame.program.functions;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

public class RandFunction extends Function {

    public static final String NAME = "rand";
    private final Random random = new Random();

    public RandFunction() {
        super(NAME);
    }

    @Override
    public Type getReturnType(ArrayList<Expression> arguments) {
        return NumberType.NUMBER_TYPE;
    }

    @Override
    public Object execute(ArrayList<Expression> arguments, Program program) {
        double minNum = (double) arguments.get(0).calculate(program);
        double maxNum = (double) arguments.get(1).calculate(program);
        return random.nextDouble()*(maxNum - minNum) + minNum;
    }

    @Override
    public ValidationResult validate(ArrayList<Expression> arguments, ParserRuleContext ctx) {
        return validateArguments(arguments, ctx, NumberType.NUMBER_TYPE, NumberType.NUMBER_TYPE);
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
