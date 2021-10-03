package se.lindhen.qrgame.program.types;

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
        return this;
    }

    @Override
    public Type inferFromGenerics(GenericTypeTracker genericTypeTracker) {
        return this;
    }

    @Override
    public String toString() {
        return "bool";
    }
}
