package se.lindhen.qrgame.program.types;

import java.util.*;
import java.util.stream.Collectors;

public class GenericType extends Type {

    private int id;
    private final ArrayList<Type> constraints = new ArrayList<>();

    public GenericType(int id) {
        super(BaseType.GENERIC);
        this.id = id;
    }

    public GenericType withConstraints(Type... typeConstraints) {
        constraints.addAll(Arrays.asList(typeConstraints));
        return this;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean acceptsType(Type sourceType) {
        return constraints.isEmpty() ||
                sourceType.isGeneric() && allTypesAreAcceptedByAnyConstraint(((GenericType)sourceType).constraints) ||
                anyConstraintAcceptsType(sourceType);
    }

    public boolean anyConstraintAcceptsType(Type sourceType) {
        for (Type constraint : constraints) {
            if (constraint.acceptsType(sourceType))
                return true;
        }
        return false;
    }

    public boolean allTypesAreAcceptedByAnyConstraint(Collection<Type> sourceTypes) {
        for (Type sourceType : sourceTypes) {
            if (!anyConstraintAcceptsType(sourceType))
                return false;
        }
        return true;
    }

    @Override
    public Type coerce(Type type, GenericTypeTracker genericTypeTracker) throws CoercionException {
        return genericTypeTracker.coerce(id, type);
    }

    @Override
    public Type inferFromGenerics(GenericTypeTracker genericTypeTracker) {
        Type inferredType = genericTypeTracker.getInferredType(id);
        return inferredType != null && acceptsType(inferredType) ? inferredType : new GenericType(id);
    }

    @Override
    protected void getUnresolvedGenerics(Set<Integer> accumulator) {
        accumulator.add(id);
    }

    @Override
    protected void remapGenerics(Map<Integer, Integer> genericRemapping) {
        id = genericRemapping.get(id);
    }

    @Override
    protected Object clone() {
        return new GenericType(id).withConstraints((Type[]) constraints.stream().map(Type::clone).toArray());
    }

    @Override
    public String toString() {
        return "generic" +
                (constraints.isEmpty() ?
                        "" :
                        "<" + constraints.stream().map(Type::toString).collect(Collectors.joining(", ")) + ">") +
                ":" + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GenericType that = (GenericType) o;
        return id == that.id && constraints.equals(that.constraints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, constraints);
    }
}
