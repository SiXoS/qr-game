package se.lindhen.qrgame.program.types;

import se.lindhen.qrgame.program.objects.QgClass;

import java.util.Objects;

public class IterableType extends Type {

    private final Type elementType;

    public IterableType(Type elementType) {
        super(BaseType.ITERABLE);
        this.elementType = elementType;
    }

    @Override
    public boolean isComparable() {
        return false;
    }

    @Override
    public boolean acceptsType(Type sourceType) {
        if (sourceType.isIterable()) {
            return elementType.acceptsType(((IterableType)sourceType).elementType);
        } else if (sourceType.isObject()) {
            ObjectType objectType = (ObjectType) sourceType;
            QgClass<?> qgClass = objectType.getQgClass();
            return qgClass.isIterable() && elementType.acceptsType(qgClass.iteratorType(objectType));
        } else {
            return false;
        }
    }

    @Override
    public Type coerce(Type type, GenericTypeTracker genericTypeTracker) {
        if (type.isIterable()) {
            return new IterableType(elementType.coerce(((IterableType)type).elementType, genericTypeTracker));
        }
        if (type.isObject()) {
            ObjectType objectType = (ObjectType) type;
            QgClass<?> qgClass = objectType.getQgClass();
            return new IterableType(elementType.coerce(qgClass.iteratorType(objectType), genericTypeTracker));
        }
        return this;
    }

    @Override
    public Type inferFromGenerics(GenericTypeTracker genericTypeTracker) {
        return new IterableType(elementType.inferFromGenerics(genericTypeTracker));
    }

    @Override
    public String toString() {
        return "iterable<" + elementType + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        IterableType that = (IterableType) o;
        return Objects.equals(elementType, that.elementType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), elementType);
    }
}
