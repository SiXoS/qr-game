package se.lindhen.qrgame.program.types;

public class VoidType extends Type {

    public static final VoidType VOID_TYPE = new VoidType();

    private VoidType() {
        super(BaseType.VOID);
    }

    @Override
    public boolean isComparable() {
        return false;
    }

    @Override
    public boolean acceptsType(Type sourceType) {
        return sourceType.isVoid();
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
        return "void";
    }
}
