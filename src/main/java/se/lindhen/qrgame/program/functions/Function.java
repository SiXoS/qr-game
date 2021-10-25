package se.lindhen.qrgame.program.functions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.FunctionType;

import java.util.*;

public abstract class Function {

    private final String name;
    private final FunctionType functionType;

    public Function(String name, FunctionType functionType) {
        this.name = name;
        this.functionType = functionType;
    }

    public abstract Object execute(List<Expression> arguments, Program program);
    public abstract Optional<Integer> getConstantParameterCount();
    public abstract boolean isConstant();

    public FunctionType getFunctionType() {
        return functionType;
    }

    public String getName() {
        return name;
    }

}
