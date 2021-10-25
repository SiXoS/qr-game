package se.lindhen.qrgame.program.types;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class StructType extends Type {

    private final int structId;

    public StructType(int structId) {
        super(BaseType.STRUCT);
        this.structId = structId;
    }

    @Override
    public boolean isComparable() {
        return false;
    }

    @Override
    public boolean acceptsType(Type sourceType) {
        return sourceType.isStruct() && structId == ((StructType) sourceType).structId;
    }

    @Override
    public Type coerce(Type type, GenericTypeTracker genericTypeTracker) {
        return (Type) type.clone();
    }

    @Override
    public Type inferFromGenerics(GenericTypeTracker genericTypeTracker) {
        return this;
    }

    @Override
    protected void getUnresolvedGenerics(Set<Integer> accumulator) { }

    @Override
    protected void remapGenerics(Map<Integer, Integer> genericRemapping) { }

    @Override
    protected Object clone() {
        return this;
    }

    public int getStructId() {
        return structId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StructType that = (StructType) o;
        return structId == that.structId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), structId);
    }

    @Override
    public String toString() {
        return "struct(" + structId + ")";
    }
}
