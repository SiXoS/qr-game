package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.utils.*;
import se.lindhen.qrgame.program.types.BoolType;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.*;

public class TreeSetClass extends QgClass<TreeSetClass.TreeSetObject> {

    public static final String NAME = "TreeSet";
    private static final TreeSetClass qgClass = new TreeSetClass();

    private TreeSetClass() {
        super(NAME, 1);
    }

    public static TreeSetClass getQgClass() {
        return qgClass;
    }

    @Override
    protected List<Method<TreeSetObject>> getMethods() {
        ArrayList<Method<TreeSetObject>> methods = new ArrayList<>();
        methods.add(new LambdaMethod<>(new ConstantGenericType(BoolType.BOOL_TYPE), "add", (obj, args, vars) -> obj.add(args.get(0).calculate(vars)), new GenericInnerType()));
        methods.add(new LambdaMethod<>(new ConstantGenericType(NumberType.NUMBER_TYPE), "size", (list, args, vars) -> list.size()));
        methods.add(new LambdaMethod<>(new ConstantGenericType(BoolType.BOOL_TYPE), "remove", (set, args, vars) -> set.remove(args.get(0).calculate(vars)), new GenericInnerType()));
        methods.add(new LambdaMethod<>(new ConstantGenericType(BoolType.BOOL_TYPE), "contains", (set, args, vars) -> set.contains(args.get(0).calculate(vars)), new GenericInnerType()));
        methods.add(new LambdaMethod<>(new GenericInnerType(), "first", (set, obj, prog) -> set.first()));
        methods.add(new LambdaMethod<>(new GenericInnerType(), "last", (set, obj, prog) -> set.last()));
        methods.add(new LambdaMethod<>(new GenericInnerType(), "next", (set, args, vars) -> set.nextValue(args.get(0).calculate(vars)), new GenericInnerType()));
        methods.add(new LambdaMethod<>(new GenericInnerType(), "previous", (set, args, vars) -> set.previousValue(args.get(0).calculate(vars)), new GenericInnerType()));
        methods.add(new ForEachMethod<>("addAll", TreeSetObject::add));
        methods.add(new ForEachMethod<>("removeAll", TreeSetObject::remove));
        return methods;
    }

    public TreeSetObject createInstance(Type innerType, TreeSet<Object> value) {
        return new TreeSetObject(innerType, value);
    }

    public Type getObjectType(List<Expression> constructorArguments) {
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

    public class TreeSetObject extends ObjectValue {

        private final TreeSet<Object> set;

        private TreeSetObject(Type innerType, TreeSet<Object> value) {
            super(TreeSetClass.NAME, new ObjectType(TreeSetClass.this, innerType));
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
            return set.first();
        }

        public Object last() {
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
