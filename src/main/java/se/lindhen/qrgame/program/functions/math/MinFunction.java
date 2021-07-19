package se.lindhen.qrgame.program.functions.math;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;

import java.util.ArrayList;
import java.util.Optional;

public class MinFunction extends Function {
    public MinFunction() {
        super("min");
    }

    @Override
    public Type getReturnType(ArrayList<Expression> arguments) {
        return NumberType.NUMBER_TYPE;
    }

    @Override
    public Object execute(ArrayList<Expression> arguments, Program program) {
        double min = Double.MAX_VALUE;
        for (Expression argument : arguments) {
            double value = (double) argument.calculate(program);
            min = Math.min(min, value);
        }
        return min;
    }

    @Override
    public ValidationResult validate(ArrayList<Expression> arguments, ParserRuleContext ctx) {
        for (int i = 0; i < arguments.size(); i++) {
            Type type = arguments.get(i).getType();
            if (!type.isNumber()) {
                return ValidationResult.invalid(ctx, "Argument " + i + " to max was type '" + type + "', expected number");
            }
        }
        return ValidationResult.valid();
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.empty();
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
