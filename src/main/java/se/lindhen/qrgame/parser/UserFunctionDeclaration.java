package se.lindhen.qrgame.parser;

import se.lindhen.qrgame.program.types.FunctionType;

public class UserFunctionDeclaration {

    private final String name;
    private final int functionId;
    private final FunctionType functionType;

    public UserFunctionDeclaration(String name, int functionId, FunctionType functionType) {
        this.name = name;
        this.functionId = functionId;
        this.functionType = functionType;
    }

    public String getName() {
        return name;
    }

    public FunctionType getFunctionType() {
        return functionType;
    }

    public int getFunctionId() {
        return functionId;
    }
}
