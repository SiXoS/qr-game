package se.lindhen.qrgame.program.functions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.*;

public abstract class Function {

    private final String name;
    private final FunctionDeclaration functionDeclaration;

    public Function(String name, FunctionDeclaration functionDeclaration) {
        this.name = name;
        this.functionDeclaration = functionDeclaration;
    }

    public abstract Object execute(List<Expression> arguments, Program program);
    public abstract Optional<Integer> getConstantParameterCount();
    public abstract boolean isConstant();

    public FunctionDeclaration getFunctionDeclaration() {
        return functionDeclaration;
    }

    public String getName() {
        return name;
    }

}
