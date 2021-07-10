package se.lindhen.qrgame.bytecode;

import se.lindhen.qrgame.program.types.Type;

public class UserFunctionData {

    private final Type returnType;
    private final int parameterCount;

    public UserFunctionData(Type returnType, int parameterCount) {
        this.returnType = returnType;
        this.parameterCount = parameterCount;
    }

    public Type getReturnType() {
        return returnType;
    }

    public int getParameterCount() {
        return parameterCount;
    }
}
