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
        super(NAME);
    }

    public static ListClass getQgClass() {
        return qgClass;
    }

    @Override
    protected List<Method<ListObject>> getMethods() {
        ArrayList<Method<ListObject>> methods = new ArrayList<>();
        ObjectType objectType = new ObjectType(this, new GenericType(0));
        methods.add(new LambdaMethod<>("get", (obj, args, vars) -> obj.get((int)(double) args.get(0).calculate(vars)), new FunctionType(new GenericType(0), objectType, NumberType.NUMBER_TYPE)));
        methods.add(new LambdaMethod<>("set", (obj, args, vars) -> obj.set((int)(double) args.get(0).calculate(vars), args.get(1).calculate(vars)), new FunctionType(VoidType.VOID_TYPE, objectType, NumberType.NUMBER_TYPE, new GenericType(0))));
        methods.add(new LambdaMethod<>("append", (obj, args, vars) -> obj.add(args.get(0).calculate(vars)), new FunctionType(VoidType.VOID_TYPE, objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>("size", (list, args, vars) -> list.size(), new FunctionType(NumberType.NUMBER_TYPE, objectType)));
        methods.add(new LambdaMethod<>("push", (list, args, vars) -> list.push(args.get(0).calculate(vars)), new FunctionType(VoidType.VOID_TYPE, objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>("pop", (list, args, vars) -> list.pop(), new FunctionType(new GenericType(0), objectType)));
        methods.add(new LambdaMethod<>("peek", (list, args, vars) -> list.peek(), new FunctionType(new GenericType(0), objectType)));
        methods.add(new LambdaMethod<>("pushLast", (list, args, vars) -> list.pushLast(args.get(0).calculate(vars)), new FunctionType(VoidType.VOID_TYPE, objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>("popLast", (list, args, vars) -> list.popLast(), new FunctionType(new GenericType(0), objectType)));
        methods.add(new LambdaMethod<>("peekLast", (list, args, vars) -> list.peekLast(), new FunctionType(new GenericType(0), objectType)));
        methods.add(new LambdaMethod<>("remove", (list, args, prog) -> list.remove(args.get(0).calculate(prog)), new FunctionType(BoolType.BOOL_TYPE, objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>("removeAt", (list, args, prog) -> list.removeAt((int)(double)args.get(0).calculate(prog)), new FunctionType(new GenericType(0), objectType, NumberType.NUMBER_TYPE)));
        methods.add(new ForEachMethod<>("addAll", ListObject::add, objectType));
        methods.add(new ForEachMethod<>("removeAll", ListObject::remove, objectType));
        return methods;
    }

    @Override
    public ArgumentCountValidation validateArgumentCount(int arguments) {
        return ArgumentCountValidation.validate(1, arguments);
    }

    public ListObject createInstance( List<Object> backingList) {
        return new ListObject(backingList);
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

    public class ListObject extends ObjectValue {

        private final List<Object> list;

        private ListObject(List<Object> backingList) {
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
            if (index >= list.size()) {
                return null;
            }
            return list.get(index);
        }

        public Object set(int index, Object element) {
            if (index >= list.size()) return null;
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

        public Object pushLast(Object calculate) {
            list.add(calculate);
            return null;
        }

        public Object popLast() {
            if (list.isEmpty()) {
                return null;
            }
            return list.remove(list.size() - 1);
        }

        public Object peekLast() {
            return list.isEmpty() ? null : list.get(list.size() - 1);
        }

        public boolean remove(Object value) {
            return list.remove(value);
        }

        public Object removeAt(int index) {
            if (index >= list.size()) return null;
            return list.remove(index);
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
