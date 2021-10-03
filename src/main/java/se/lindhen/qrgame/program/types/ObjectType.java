package se.lindhen.qrgame.program.types;

import se.lindhen.qrgame.program.objects.QgClass;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ObjectType extends Type {

    private final QgClass<?> qgClass;
    private final List<Type> innerTypes;

    public ObjectType(QgClass<?> qgClass, Type... innerTypes) {
        super(BaseType.OBJECT);
        assert Arrays.stream(innerTypes).noneMatch(Type::isVararg): "Objects cannot have vararg type parameters"; // acceptsType() does not handle vararg
        this.qgClass = qgClass;
        this.innerTypes = Arrays.asList(innerTypes);
    }

    @Override
    public boolean isComparable() {
        return false;
    }

    @Override
    public boolean acceptsType(Type sourceType) {
        if (!sourceType.isObject()) return false;

        ObjectType objectType = (ObjectType) sourceType;

        if (!qgClass.equals(objectType.qgClass)) return false;
        if (innerTypes.size() != objectType.innerTypes.size()) return false;

        for (int i = 0; i < innerTypes.size(); i++) {
            if (!innerTypes.get(i).acceptsType(objectType.innerTypes.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Type coerce(Type type, GenericTypeTracker genericTypeTracker) {
        if (!type.isObject()) return this;
        ObjectType objectType = (ObjectType) type;
        Type[] resultingInnerTypes = new Type[Math.min(innerTypes.size(), objectType.innerTypes.size())];
        for (int i = 0; i < resultingInnerTypes.length; i++) {
            resultingInnerTypes[i] = innerTypes.get(i).coerce(objectType.innerTypes.get(i), genericTypeTracker);
        }
        return new ObjectType(qgClass, resultingInnerTypes);
    }

    @Override
    public Type inferFromGenerics(GenericTypeTracker genericTypeTracker) {
        Type[] newInnerTypes = new Type[innerTypes.size()];
        for (int i = 0; i < innerTypes.size(); i++) {
            newInnerTypes[i] = innerTypes.get(i).inferFromGenerics(genericTypeTracker);
        }
        return new ObjectType(qgClass, newInnerTypes);
    }

    public List<Type> getInnerTypes() {
        return innerTypes;
    }

    public QgClass<?> getQgClass() {
        return qgClass;
    }

    @Override
    public String toString() {
        String innerTypeString = "";
        if (!innerTypes.isEmpty()) {
            innerTypeString = innerTypes.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(",", "<", ">")) ;
        }
        return qgClass.getName() + innerTypeString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ObjectType that = (ObjectType) o;
        return Objects.equals(qgClass, that.qgClass) &&
                Objects.equals(innerTypes, that.innerTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), qgClass, innerTypes);
    }
}
