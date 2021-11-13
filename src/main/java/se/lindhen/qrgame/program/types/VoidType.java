package se.lindhen.qrgame.program.types;

import java.util.Map;
import java.util.Set;

public class VoidType extends Type {

    public static final VoidType VOID_TYPE = new VoidType();

    private VoidType() {
        super(BaseType.VOID);
    }

    @Override
    public boolean acceptsType(Type sourceType) {
        return sourceType.isVoid();
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
        return "void";
    }
}
