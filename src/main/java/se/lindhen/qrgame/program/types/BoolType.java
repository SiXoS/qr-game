package se.lindhen.qrgame.program.types;

import java.util.Map;
import java.util.Set;

public class BoolType extends Type {

    public final static BoolType BOOL_TYPE = new BoolType();

    private BoolType() {
        super(BaseType.BOOL);
    }

    @Override
    public boolean isComparable() {
        return true;
    }

    @Override
    public boolean acceptsType(Type sourceType) {
        return sourceType.isBool();
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

    @Override
    public String toString() {
        return "bool";
    }
}
