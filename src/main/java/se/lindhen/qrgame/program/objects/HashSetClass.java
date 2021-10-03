package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.utils.*;
import se.lindhen.qrgame.program.types.BoolType;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.*;

public class HashSetClass extends QgClass<HashSetClass.HashSetObject> {

    public static final String NAME = "HashSet";
    private static final HashSetClass qgClass = new HashSetClass();

    private HashSetClass() {
        super(NAME);
    }

    public static HashSetClass getQgClass() {
        return qgClass;
    }

    @Override
    protected List<Method<HashSetObject>> getMethods() {
        ArrayList<Method<HashSetObject>> methods = new ArrayList<>();
        methods.add(new LambdaMethod<>(new ConstantGenericType(BoolType.BOOL_TYPE), "add", (obj, args, vars) -> obj.add(args.get(0).calculate(vars)), new GenericInnerType()));
        methods.add(new LambdaMethod<>(new ConstantGenericType(NumberType.NUMBER_TYPE), "size", (set, args, vars) -> set.size()));
        methods.add(new LambdaMethod<>(new ConstantGenericType(BoolType.BOOL_TYPE), "remove", (set, args, vars) -> set.remove(args.get(0).calculate(vars)), new GenericInnerType()));
        methods.add(new LambdaMethod<>(new ConstantGenericType(BoolType.BOOL_TYPE), "contains", (set, args, vars) -> set.contains(args.get(0).calculate(vars)), new GenericInnerType()));
        methods.add(new ForEachMethod<>("addAll", HashSetObject::add));
        methods.add(new ForEachMethod<>("removeAll", HashSetObject::remove));
        return methods;
    }

    @Override
    public ArgumentCountValidation validateArgumentCount(int arguments) {
        return ArgumentCountValidation.validate(1, arguments);
    }

    public HashSetObject createInstance(HashSet<Object> value) {
        return new HashSetObject(value);
    }

    public Type getObjectType(List<Type> constructorArguments) {
        return new ObjectType(this, CollectionConstructionUtils.innerTypeFromArguments(constructorArguments));
    }

    @Override
    public Type getObjectTypeFromTypeArgs(List<Type> constructorArguments) {
        return new ObjectType(this, constructorArguments.get(0));
    }

    @Override
    public boolean isIterable() {
        return true;
    }

    @Override
    public Type iteratorType(ObjectType objectType) {
        return objectType.getInnerTypes().get(0);
    }

    @Override
    public boolean isComparable() {
        return false;
    }

    public class HashSetObject extends ObjectValue {

        private final HashSet<Object> set;

        private HashSetObject(HashSet<Object> value) {
            super(HashSetClass.NAME);
            this.set = value;
        }

        @Override
        public Object execute(int methodId, List<Expression> arguments, Program program) {
            return HashSetClass.this.execute(this, methodId, arguments, program);
        }

        @Override
        public Iterator<Object> iterator() {
            return set.iterator();
        }

        public boolean add(Object element) {
            return set.add(element);
        }

        public double size() {
            return set.size();
        }

        public boolean remove(Object item) {
            return set.remove(item);
        }

        public boolean contains(Object item) {
            return set.contains(item);
        }

        public HashSet<Object> getBackingSet() {
            return set;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HashSetObject that = (HashSetObject) o;
            return set.equals(that.set);
        }

        @Override
        public int hashCode() {
            return set.hashCode();
        }
    }
}
