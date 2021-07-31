package se.lindhen.qrgame.program.functions;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.BoolType;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.types.VoidType;

import java.util.ArrayList;
import java.util.Optional;

public class AssertFunction extends Function {

    public static final String NAME = "assert";

    public AssertFunction() {
        super(NAME);
    }

    @Override
    public Type getReturnType(ArrayList<Expression> arguments) {
        return VoidType.VOID_TYPE;
    }

    @Override
    public Object execute(ArrayList<Expression> arguments, Program program) {
        assert (boolean) arguments.get(1).calculate(program): "Assertion '" + arguments.get(0).calculate(program) + "' failed";
        return null;
    }

    @Override
    public ValidationResult validate(ArrayList<Expression> arguments, ParserRuleContext ctx) {
        return validateArguments(arguments, ctx, NumberType.NUMBER_TYPE, BoolType.BOOL_TYPE);
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
