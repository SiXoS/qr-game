package se.lindhen.qrgame.program.functions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.BoolType;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.VoidType;

import java.util.List;
import java.util.Optional;

public class AssertFunction extends Function {

    public static final String NAME = "assert";

    public AssertFunction() {
        super(NAME, new FunctionType(VoidType.VOID_TYPE, NumberType.NUMBER_TYPE, BoolType.BOOL_TYPE));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        assert (boolean) arguments.get(1).calculate(program): "Assertion '" + arguments.get(0).calculate(program) + "' failed";
        return null;
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
