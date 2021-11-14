package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.utils.*;
import se.lindhen.qrgame.program.types.*;
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
        ObjectType objectType = new ObjectType(this, new GenericType(0));
        methods.add(new LambdaMethod<>("add", (obj, args, vars) -> obj.add(args.get(0).calculate(vars)), new FunctionType(BoolType.BOOL_TYPE, objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>("size", (set, args, vars) -> set.size(), new FunctionType(NumberType.NUMBER_TYPE, objectType)));
        methods.add(new LambdaMethod<>("remove", (set, args, vars) -> set.remove(args.get(0).calculate(vars)), new FunctionType(BoolType.BOOL_TYPE, objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>("contains", (set, args, vars) -> set.contains(args.get(0).calculate(vars)), new FunctionType(BoolType.BOOL_TYPE, objectType, new GenericType(0))));
        methods.add(new ForEachMethod<>("addAll", HashSetObject::add, objectType));
        methods.add(new ForEachMethod<>("removeAll", HashSetObject::remove, objectType));
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

    public class HashSetObject extends ObjectValue {

        private final HashSet<Object> set;

        private HashSetObject(HashSet<Object> value) {
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
