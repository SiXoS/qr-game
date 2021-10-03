package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.utils.*;
import se.lindhen.qrgame.program.types.BoolType;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.util.IndexedHashSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IndexedHashSetClass extends QgClass<IndexedHashSetClass.IndexedHashSetObject> {

    public static final String NAME = "IndexedHashSet";
    private static final IndexedHashSetClass qgClass = new IndexedHashSetClass();

    private IndexedHashSetClass() {
        super(NAME);
    }

    public static IndexedHashSetClass getQgClass() {
        return qgClass;
    }

    @Override
    protected List<Method<IndexedHashSetObject>> getMethods() {
        ArrayList<Method<IndexedHashSetObject>> methods = new ArrayList<>();
        methods.add(new LambdaMethod<>(new ConstantGenericType(BoolType.BOOL_TYPE), "add", (obj, args, vars) -> obj.add(args.get(0).calculate(vars)), new GenericInnerType()));
        methods.add(new LambdaMethod<>(new ConstantGenericType(NumberType.NUMBER_TYPE), "size", (set, args, vars) -> set.size()));
        methods.add(new LambdaMethod<>(new ConstantGenericType(BoolType.BOOL_TYPE), "remove", (set, args, vars) -> set.remove(args.get(0).calculate(vars)), new GenericInnerType()));
        methods.add(new LambdaMethod<>(new ConstantGenericType(BoolType.BOOL_TYPE), "contains", (set, args, vars) -> set.contains(args.get(0).calculate(vars)), new GenericInnerType()));
        methods.add(new LambdaMethod<>(new GenericInnerType(), "get", (set, args, vars) -> set.get((int)(double)args.get(0).calculate(vars)), new ConstantGenericType(NumberType.NUMBER_TYPE)));
        methods.add(new LambdaMethod<>(new GenericInnerType(), "removeAt", (set, args, vars) -> set.removeAt((int)(double)args.get(0).calculate(vars)), new ConstantGenericType(NumberType.NUMBER_TYPE)));
        methods.add(new ForEachMethod<>("addAll", IndexedHashSetObject::add));
        methods.add(new ForEachMethod<>("removeAll", IndexedHashSetObject::remove));
        return methods;
    }

    @Override
    public ArgumentCountValidation validateArgumentCount(int arguments) {
        return ArgumentCountValidation.validate(1, arguments);
    }

    public IndexedHashSetObject createInstance(IndexedHashSet<Object> value) {
        return new IndexedHashSetObject(value);
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

    public class IndexedHashSetObject extends ObjectValue {

        private final IndexedHashSet<Object> set;

        private IndexedHashSetObject(IndexedHashSet<Object> value) {
            super(IndexedHashSetClass.NAME);
            this.set = value;
        }

        @Override
        public Object execute(int methodId, List<Expression> arguments, Program program) {
            return IndexedHashSetClass.this.execute(this, methodId, arguments, program);
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

        public Object get(int index) {
            return set.get(index);
        }

        public Object removeAt(int index) {
            return set.removeAt(index);
        }

        public IndexedHashSet<Object> getBackingSet() {
            return set;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IndexedHashSetObject that = (IndexedHashSetObject) o;
            return set.equals(that.set);
        }

        @Override
        public int hashCode() {
            return set.hashCode();
        }
    }
}
