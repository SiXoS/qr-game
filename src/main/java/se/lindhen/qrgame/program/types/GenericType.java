package se.lindhen.qrgame.program.types;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class GenericType extends Type {

    private int id;

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
    public Type coerce(Type type, GenericTypeTracker genericTypeTracker) throws CoercionException {
        return genericTypeTracker.coerce(id, type);
    }

    @Override
    public Type inferFromGenerics(GenericTypeTracker genericTypeTracker) {
        Type inferredType = genericTypeTracker.getInferredType(id);
        return inferredType != null ? inferredType : new GenericType(id);
    }

    @Override
    protected void getUnresolvedGenerics(Set<Integer> accumulator) {
        accumulator.add(id);
    }

    @Override
    protected void remapGenerics(Map<Integer, Integer> genericRemapping) {
        id = genericRemapping.get(id);
    }

    @Override
    protected Object clone() {
        return new GenericType(id);
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
