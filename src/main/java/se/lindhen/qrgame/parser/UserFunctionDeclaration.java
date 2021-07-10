package se.lindhen.qrgame.parser;

import se.lindhen.qrgame.program.types.Type;

import java.util.ArrayList;

public class UserFunctionDeclaration {

    private final String name;
    private final int functionId;
    private final ArrayList<FunctionParameterDeclaration> parameters;
    private final Type returnType;

    public UserFunctionDeclaration(String name, int functionId, ArrayList<FunctionParameterDeclaration> parameters, Type returnType) {
        this.name = name;
        this.functionId = functionId;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public Type getReturnType() {
        return returnType;
    }

    public ArrayList<FunctionParameterDeclaration> getParameters() {
        return parameters;
    }

    public int getFunctionId() {
        return functionId;
    }
}
