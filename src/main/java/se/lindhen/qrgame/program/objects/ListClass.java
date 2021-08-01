package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.utils.*;
import se.lindhen.qrgame.program.types.*;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListClass extends QgClass<ListClass.ListObject> {

    public static final String NAME = "List";
    private static final ListClass qgClass = new ListClass();

    private ListClass() {
        super(NAME, 1);
    }

    public static ListClass getQgClass() {
        return qgClass;
    }

    @Override
    protected List<Method<ListObject>> getMethods() {
        ArrayList<Method<ListObject>> methods = new ArrayList<>();
        methods.add(new LambdaMethod<>(new GenericInnerType(), "get", (obj, args, vars) -> obj.get((int)(double) args.get(0).calculate(vars)), new ConstantGenericType(NumberType.NUMBER_TYPE)));
        methods.add(new LambdaMethod<>(new ConstantGenericType(VoidType.VOID_TYPE), "set", (obj, args, vars) -> obj.set((int)(double) args.get(0).calculate(vars), args.get(1).calculate(vars)), new ConstantGenericType(NumberType.NUMBER_TYPE), new GenericInnerType()));
        methods.add(new LambdaMethod<>(new ConstantGenericType(VoidType.VOID_TYPE), "append", (obj, args, vars) -> obj.add(args.get(0).calculate(vars)), new GenericInnerType()));
        methods.add(new LambdaMethod<>(new ConstantGenericType(NumberType.NUMBER_TYPE), "size", (list, args, vars) -> list.size()));
        methods.add(new LambdaMethod<>(new ConstantGenericType(VoidType.VOID_TYPE), "push", (list, args, vars) -> list.push(args.get(0).calculate(vars)), new GenericInnerType()));
        methods.add(new LambdaMethod<>(new GenericInnerType(), "pop", (list, args, vars) -> list.pop()));
        methods.add(new LambdaMethod<>(new GenericInnerType(), "peek", (list, args, vars) -> list.peek()));
        methods.add(new LambdaMethod<>(new ConstantGenericType(BoolType.BOOL_TYPE), "remove", (list, args, prog) -> list.remove(args.get(0).calculate(prog)), new GenericInnerType()));
        methods.add(new ForEachMethod<>("addAll", ListObject::add));
        methods.add(new ForEachMethod<>("removeAll", ListObject::remove));
        return methods;
    }

    public ListObject createInstance(Type innerType, List<Object> backingList) {
        return new ListObject(new ObjectType(this, innerType), backingList);
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

    public class ListObject extends ObjectValue {

        private final List<Object> list;

        private ListObject(ObjectType type, List<Object> backingList) {
            super(ListClass.NAME, type);
            this.list = backingList;
        }

        @Override
        public Object execute(int methodId, List<Expression> arguments, Program program) {
            return ListClass.this.execute(this, methodId, arguments, program);
        }

        @Override
        public Iterator<Object> iterator() {
            return list.iterator();
        }

        public Object get(int index) {
            if (index >= size()) {
                return null;
            }
            return list.get(index);
        }

        public Object set(int index, Object element) {
            list.set(index, element);
            return null;
        }

        public Object add(Object element) {
            list.add(element);
            return null;
        }

        public double size() {
            return list.size();
        }

        public Object push(Object calculate) {
            list.add(0, calculate);
            return null;
        }

        public Object pop() {
            if (list.isEmpty()) {
                return null;
            }
            return list.remove(0);
        }

        public Object peek() {
            return list.isEmpty() ? null : list.get(0);
        }

        public boolean remove(Object value) {
            return list.remove(value);
        }

        public List<Object> getBackingList() {
            return list;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ListObject that = (ListObject) o;
            return list.equals(that.list);
        }

        @Override
        public int hashCode() {
            return list.hashCode();
        }
    }
}
