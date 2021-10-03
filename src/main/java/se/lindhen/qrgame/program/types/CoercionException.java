package se.lindhen.qrgame.program.types;

public class CoercionException extends RuntimeException { // TODO not runtime exception
    public final Type preExistingType;
    public final Type newType;
    public final int genericTypeId;

    public CoercionException(Type preExistingType, Type newType, int genericTypeId) {
        this.preExistingType = preExistingType;
        this.newType = newType;
        this.genericTypeId = genericTypeId;
    }
}
