package se.lindhen.qrgame.program.functions.math;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.functions.FunctionDeclaration;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BinaryMathFunction extends Function {

    public BinaryMathFunction(String name) {
        super(name, new FunctionDeclaration(0, NumberType.NUMBER_TYPE, NumberType.NUMBER_TYPE, NumberType.NUMBER_TYPE));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        return calculate((double) arguments.get(0).calculate(program), (double) arguments.get(1).calculate(program));
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(2);
    }

    public abstract double calculate(double arg1, double arg2);

    @Override
    public boolean isConstant() {
        return true;
    }
}
