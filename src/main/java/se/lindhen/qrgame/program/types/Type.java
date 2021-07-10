package se.lindhen.qrgame.program.types;

import java.util.Objects;

public abstract class Type {

    private final BaseType baseType;

    protected Type(BaseType baseType) {
        this.baseType = baseType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return baseType == type.baseType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseType);
    }

    public boolean isNumber() {
        return baseType == BaseType.NUMBER;
    }
    public boolean isBool() {
        return baseType == BaseType.BOOL;
    }
    public boolean isType() { return baseType == BaseType.TYPE; }
    public boolean isObject() { return baseType == BaseType.OBJECT; }
    public boolean isStruct() { return baseType == BaseType.STRUCT; }
    public boolean isVoid() { return baseType == BaseType.VOID; }

    public abstract boolean isComparable();

    public BaseType getBaseType() {
        return baseType;
    }

    public enum BaseType {
        NUMBER,
        BOOL,
        STRUCT,
        VOID,
        TYPE,
        OBJECT
    }
}
