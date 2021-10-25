package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.utils.*;
import se.lindhen.qrgame.program.types.*;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.*;

public class TreeSetClass extends QgClass<TreeSetClass.TreeSetObject> {

    public static final String NAME = "TreeSet";
    private static final TreeSetClass qgClass = new TreeSetClass();

    private TreeSetClass() {
        super(NAME);
    }

    public static TreeSetClass getQgClass() {
        return qgClass;
    }

    @Override
    public ArgumentCountValidation validateArgumentCount(int arguments) {
        return ArgumentCountValidation.validate(1, arguments);
    }

    @Override
    protected List<Method<TreeSetObject>> getMethods() {
        ArrayList<Method<TreeSetObject>> methods = new ArrayList<>();
        ObjectType objectType = new ObjectType(this, new GenericType(0));
        methods.add(new LambdaMethod<>( "add", (obj, args, vars) -> obj.add(args.get(0).calculate(vars)), new FunctionType(BoolType.BOOL_TYPE, objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>( "size", (list, args, vars) -> list.size(), new FunctionType(NumberType.NUMBER_TYPE, objectType)));
        methods.add(new LambdaMethod<>( "remove", (set, args, vars) -> set.remove(args.get(0).calculate(vars)), new FunctionType(BoolType.BOOL_TYPE, objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>( "contains", (set, args, vars) -> set.contains(args.get(0).calculate(vars)), new FunctionType(BoolType.BOOL_TYPE, objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>( "first", (set, obj, prog) -> set.first(), new FunctionType(new GenericType(0), objectType)));
        methods.add(new LambdaMethod<>( "last", (set, obj, prog) -> set.last(), new FunctionType(new GenericType(0), objectType)));
        methods.add(new LambdaMethod<>( "next", (set, args, vars) -> set.nextValue(args.get(0).calculate(vars)), new FunctionType(new GenericType(0), objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>( "previous", (set, args, vars) -> set.previousValue(args.get(0).calculate(vars)), new FunctionType(new GenericType(0), objectType, new GenericType(0))));
        methods.add(new ForEachMethod<>("addAll", TreeSetObject::add, objectType));
        methods.add(new ForEachMethod<>("removeAll", TreeSetObject::remove, objectType));
        return methods;
    }

    public TreeSetObject createInstance(TreeSet<Object> value) {
        return new TreeSetObject(value);
    }

    @Override
    public Type getObjectTypeFromTypeArgs(List<Type> constructorArguments) {
        return new ObjectType(this, constructorArguments.get(0));
    }

    public Type getObjectType(List<Type> constructorArguments) {
        return new ObjectType(this, CollectionConstructionUtils.innerTypeFromArguments(constructorArguments));
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

    public class TreeSetObject extends ObjectValue {

        private final TreeSet<Object> set;

        private TreeSetObject(TreeSet<Object> value) {
            super(TreeSetClass.NAME);
            this.set = value;
        }

        @Override
        public Object execute(int methodId, List<Expression> arguments, Program program) {
            return TreeSetClass.this.execute(this, methodId, arguments, program);
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

        public Object nextValue(Object item) {
            return set.higher(item);
        }

        public Object previousValue(Object item) {
            return set.lower(item);
        }

        public Object first() {
            if (set.isEmpty()) return null;
            return set.first();
        }

        public Object last() {
            if (set.isEmpty()) return null;
            return set.last();
        }

        public TreeSet<Object> getBackingSet() {
            return set;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TreeSetObject that = (TreeSetObject) o;
            return set.equals(that.set);
        }

        @Override
        public int hashCode() {
            return set.hashCode();
        }
    }
}
