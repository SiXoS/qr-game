package se.lindhen.qrgame.program.types;

import se.lindhen.qrgame.program.objects.QgClass;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ObjectType extends Type {

    private final QgClass<?> qgClass;
    private List<Type> innerTypes;

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
