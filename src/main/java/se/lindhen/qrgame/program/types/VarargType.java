package se.lindhen.qrgame.program.types;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class VarargType extends Type {

    private final Type inner;

    public VarargType(Type inner) {
        super(BaseType.VARARG);
        assert !inner.isVararg(): "Vararg type cannot contain another vararg";
        this.inner = inner;
    }

    @Override
    public boolean acceptsType(Type sourceType) {
        return sourceType.isVararg() && inner.acceptsType(((VarargType)sourceType).inner) || inner.acceptsType(sourceType);
    }

    @Override
    public Type coerce(Type type, GenericTypeTracker genericTypeTracker) throws CoercionException {
        if (type.isVararg()) {
            return new VarargType(inner.coerce(((VarargType)type).inner, genericTypeTracker));
        } else {
            return new VarargType(inner.coerce(type, genericTypeTracker));
        }
    }

    @Override
    public Type inferFromGenerics(GenericTypeTracker genericTypeTracker) {
        return new VarargType(inner.inferFromGenerics(genericTypeTracker));
    }

    @Override
    protected void getUnresolvedGenerics(Set<Integer> accumulator) {
        inner.getUnresolvedGenerics(accumulator);
    }

    @Override
    protected void remapGenerics(Map<Integer, Integer> genericRemapping) {
        inner.remapGenerics(genericRemapping);
    }

    @Override
    protected Object clone() {
        return new VarargType((Type) inner.clone());
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
