package se.lindhen.qrgame.parser;

import se.lindhen.qrgame.program.types.Type;

public class FunctionParameterDeclaration {

    private final Type type;
    private final String varName;

    public FunctionParameterDeclaration(Type type, String varName) {
        this.type = type;
        this.varName = varName;
    }

    public Type getType() {
        return type;
    }

    public String getVarName() {
        return varName;
    }
}
