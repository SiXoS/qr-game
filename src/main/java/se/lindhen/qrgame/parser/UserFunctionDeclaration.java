package se.lindhen.qrgame.parser;

import se.lindhen.qrgame.program.functions.FunctionDeclaration;

public class UserFunctionDeclaration {

    private final String name;
    private final int functionId;
    private final FunctionDeclaration functionDeclaration;

    public UserFunctionDeclaration(String name, int functionId, FunctionDeclaration functionDeclaration) {
        this.name = name;
        this.functionId = functionId;
        this.functionDeclaration = functionDeclaration;
    }

    public String getName() {
        return name;
    }

    public FunctionDeclaration getFunctionDeclaration() {
        return functionDeclaration;
    }

    public int getFunctionId() {
        return functionId;
    }
}
