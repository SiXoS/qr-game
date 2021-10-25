package se.lindhen.qrgame.program.types;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class TypeType extends Type {

    private final Type actualType;

    public TypeType(Type actualType) {
        super(BaseType.TYPE);
        this.actualType = actualType;
    }

    @Override
    public boolean isComparable() {
        return false;
    }

    @Override
    public boolean acceptsType(Type sourceType) {
        return sourceType.isType() && actualType.acceptsType(((TypeType) sourceType).actualType);
    }

    @Override
    public Type coerce(Type type, GenericTypeTracker genericTypeTracker) throws CoercionException {
        if (!type.isType()) return (Type) type.clone();
        return new TypeType(actualType.coerce(((TypeType)type).actualType, genericTypeTracker));
    }

    @Override
    public Type inferFromGenerics(GenericTypeTracker genericTypeTracker) {
        return new TypeType(actualType.inferFromGenerics(genericTypeTracker));
    }

    @Override
    protected void getUnresolvedGenerics(Set<Integer> accumulator) {
        actualType.getUnresolvedGenerics(accumulator);
    }

    @Override
    protected void remapGenerics(Map<Integer, Integer> genericRemapping) {
        actualType.remapGenerics(genericRemapping);
    }

    @Override
    protected Object clone() {
        return new TypeType((Type) actualType.clone());
    }

    public Type getActualType() {
        return actualType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TypeType typeType = (TypeType) o;
        return Objects.equals(actualType, typeType.actualType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), actualType);
    }

    @Override
    public String toString() {
        return "typeValue";
    }
}
