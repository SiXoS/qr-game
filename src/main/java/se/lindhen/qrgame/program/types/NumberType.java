package se.lindhen.qrgame.program.types;

import java.util.Map;
import java.util.Set;

public class NumberType extends Type {

    public static final NumberType NUMBER_TYPE = new NumberType();

    private NumberType() {
        super(BaseType.NUMBER);
    }

    @Override
    public boolean acceptsType(Type sourceType) {
        return sourceType.isNumber();
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
        return "number";
    }

}
