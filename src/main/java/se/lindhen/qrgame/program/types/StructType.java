package se.lindhen.qrgame.program.types;

import java.util.Objects;

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
        return this;
    }

    @Override
    public Type inferFromGenerics(GenericTypeTracker genericTypeTracker) {
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
