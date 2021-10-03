package se.lindhen.qrgame.program.types;

public class GenericType extends Type {

    private final int id;

    public GenericType(int id) {
        super(BaseType.GENERIC);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean isComparable() {
        return false;
    }

    @Override
    public boolean acceptsType(Type sourceType) {
        return true;
    }

    @Override
    public Type coerce(Type type, GenericTypeTracker genericTypeTracker) {
        return genericTypeTracker.coerce(id, type);
    }

    @Override
    public Type inferFromGenerics(GenericTypeTracker genericTypeTracker) {
        return genericTypeTracker.getInferredType(id);
    }

}
