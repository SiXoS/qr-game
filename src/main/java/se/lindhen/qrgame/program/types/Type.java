package se.lindhen.qrgame.program.types;

import java.util.*;

public abstract class Type implements Cloneable {

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

    public final boolean isNumber() { return baseType == BaseType.NUMBER; }
    public final boolean isBool() { return baseType == BaseType.BOOL; }
    public final boolean isType() { return baseType == BaseType.TYPE; }
    public final boolean isObject() { return baseType == BaseType.OBJECT; }
    public final boolean isStruct() { return baseType == BaseType.STRUCT; }
    public final boolean isVoid() { return baseType == BaseType.VOID; }
    public final boolean isVararg() { return baseType == BaseType.VARARG; }
    public final boolean isGeneric() {  return baseType == BaseType.GENERIC; }
    public final boolean isIterable() {  return baseType == BaseType.ITERABLE; }
    public final boolean isFunction() {  return baseType == BaseType.FUNCTION; }
    public final boolean isComparable() {  return baseType == BaseType.COMPARABLE; }

    public abstract boolean acceptsType(Type sourceType);
    public abstract Type coerce(Type type, GenericTypeTracker genericTypeTracker) throws CoercionException;
    public abstract Type inferFromGenerics(GenericTypeTracker genericTypeTracker);
    protected abstract void getUnresolvedGenerics(Set<Integer> accumulator);
    protected abstract void remapGenerics(Map<Integer, Integer> genericRemapping);

    public final TreeSet<Integer> getUnresolvedGenerics() {
        TreeSet<Integer> accumulator = new TreeSet<>();
        getUnresolvedGenerics(accumulator);
        return accumulator;
    }

    public boolean canBeAssignedTo(Type targetType) {
        return targetType.acceptsType(this);
    }

    public BaseType getBaseType() {
        return baseType;
    }

    @Override
    protected abstract Object clone();

    public enum BaseType {
        NUMBER,
        BOOL,
        STRUCT,
        VOID,
        TYPE,
        OBJECT,
        VARARG,
        GENERIC,
        ITERABLE,
        FUNCTION,
        COMPARABLE
    }
}
