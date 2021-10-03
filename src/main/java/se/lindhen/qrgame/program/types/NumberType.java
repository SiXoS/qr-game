package se.lindhen.qrgame.program.types;

public class NumberType extends Type {

    public static final NumberType NUMBER_TYPE = new NumberType();

    private NumberType() {
        super(BaseType.NUMBER);
    }

    @Override
    public boolean isComparable() {
        return true;
    }

    @Override
    public boolean acceptsType(Type sourceType) {
        return sourceType.isNumber();
    }

    @Override
    public Type coerce(Type type, GenericTypeTracker genericTypeTracker) {
        return this;
    }

    @Override
    public Type inferFromGenerics(GenericTypeTracker genericTypeTracker) {
        return this;
    }

    @Override
    public String toString() {
        return "number";
    }

}
