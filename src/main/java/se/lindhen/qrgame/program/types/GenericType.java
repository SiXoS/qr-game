package se.lindhen.qrgame.program.types;

import java.util.Objects;

public class GenericType extends Type {

    private final int id;

    public GenericType(int id) {
        super(BaseType.GENERIC);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean isComparable() {
        return false;
    }

    @Override
    public boolean acceptsType(Type sourceType) {
        return true;
    }

    @Override
    public Type coerce(Type type, GenericTypeTracker genericTypeTracker) {
        return genericTypeTracker.coerce(id, type);
    }

    @Override
    public Type inferFromGenerics(GenericTypeTracker genericTypeTracker) {
        return genericTypeTracker.getInferredType(id);
    }

    @Override
    public String toString() {
        return "generic:" + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GenericType that = (GenericType) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
