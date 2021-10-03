package se.lindhen.qrgame.program.types;

import java.util.Objects;

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
    public Type coerce(Type type, GenericTypeTracker genericTypeTracker) {
        if (!type.isType()) return this;
        return new TypeType(actualType.coerce(((TypeType)type).actualType, genericTypeTracker));
    }

    @Override
    public Type inferFromGenerics(GenericTypeTracker genericTypeTracker) {
        return new TypeType(actualType.inferFromGenerics(genericTypeTracker));
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
