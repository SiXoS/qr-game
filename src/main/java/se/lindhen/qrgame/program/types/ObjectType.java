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
        this.qgClass = qgClass;
        this.innerTypes = Arrays.asList(innerTypes);
    }

    @Override
    public boolean isComparable() {
        return false;
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
