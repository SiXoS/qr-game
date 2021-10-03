package se.lindhen.qrgame.program.types;

import se.lindhen.qrgame.program.objects.ListClass;

import java.util.Objects;

public class VarargType extends Type {

    private final Type inner;

    public VarargType(Type inner) {
        super(BaseType.VARARG);
        assert !inner.isVararg(): "Vararg type cannot contain another vararg";
        this.inner = inner;
    }

    @Override
    public boolean isComparable() {
        return false;
    }

    @Override
    public boolean acceptsType(Type sourceType) {
        return sourceType.isVararg() && inner.acceptsType(((VarargType)sourceType).inner) || inner.acceptsType(sourceType);
    }

    @Override
    public Type coerce(Type type, GenericTypeTracker genericTypeTracker) {
        return new VarargType(inner.coerce(type, genericTypeTracker));
    }

    @Override
    public Type inferFromGenerics(GenericTypeTracker genericTypeTracker) {
        return new VarargType(inner.inferFromGenerics(genericTypeTracker));
    }

    @Override
    public String toString() {
        return "vararg<" + inner + ">";
    }

    public Type getInner() {
        return inner;
    }

    public static boolean canContain(Type innerType) {
        return !(innerType.isVoid() || innerType.isVararg());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VarargType that = (VarargType) o;
        return inner.equals(that.inner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), inner);
    }
}
