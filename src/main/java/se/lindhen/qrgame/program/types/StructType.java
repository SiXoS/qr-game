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
}
