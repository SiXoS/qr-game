package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.utils.*;
import se.lindhen.qrgame.program.types.*;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.*;

public class HashMapClass extends QgClass<HashMapClass.HashMapValue> {

    private static final String NAME = "HashMap";
    private static final HashMapClass instance = new HashMapClass();

    private HashMapClass() {
        super(NAME, 2);
    }

    public static HashMapClass getQgClass() {
        return instance;
    }

    @Override
    protected List<Method<HashMapValue>> getMethods() {
        ArrayList<Method<HashMapValue>> methods = new ArrayList<>();
        methods.add(new LambdaMethod<>(new ConstantGenericType(NumberType.NUMBER_TYPE), "size", (obj, args, prog) -> obj.size()));
        methods.add(new LambdaMethod<>(new GenericInnerType(1), "get", (obj, args, prog) -> obj.get(args.get(0).calculate(prog)), new GenericInnerType(0)));
        methods.add(new LambdaMethod<>(new GenericInnerType(1), "getOrDefault", (obj, args, prog) -> obj.getOrDefault(args.get(0).calculate(prog), args.get(1).calculate(prog)), new GenericInnerType(0), new GenericInnerType(1)));
        methods.add(new LambdaMethod<>(new ConstantGenericType(VoidType.VOID_TYPE), "put", (obj, args, prog) -> obj.put(args.get(0).calculate(prog), args.get(1).calculate(prog)), new GenericInnerType(0), new GenericInnerType(1)));
        methods.add(new LambdaMethod<>(new ConstantGenericType(BoolType.BOOL_TYPE), "containsKey", (obj, args, prog) -> obj.containsKey(args.get(0).calculate(prog)), new GenericInnerType(0)));
        methods.add(new LambdaMethod<>(new ConstantGenericType(BoolType.BOOL_TYPE), "removeKey", (obj, args, prog) -> obj.removeKey(args.get(0).calculate(prog)), new GenericInnerType(0)));
        methods.add(new LambdaMethod<>(new ConstantGenericType(BoolType.BOOL_TYPE), "remove", (obj, args, prog) -> obj.remove(args.get(0).calculate(prog), args.get(1).calculate(prog)), new GenericInnerType(0), new GenericInnerType(1)));
        methods.add(new LambdaMethod<>(new ListWithValueTypeAsInnerType(), "values", (obj, args, prog) -> obj.values()));
        methods.add(new LambdaMethod<>(new SetWithKeyTypeAsInnerType(HashSetClass.getQgClass()), "keys", (obj, args, prog) -> obj.keys()));
        methods.add(new ForEachMapEntryMethod<>("putAll", HashMapValue::put));
        methods.add(new ForEachMapEntryMethod<>("removeAll", HashMapValue::remove));
        methods.add(new ForEachMethod<>("removeAllKeys", HashMapValue::removeKey));
        return methods;
    }

    public HashMapValue createInstance(ObjectType objectType, HashMap<Object, Object> map) {
        return new HashMapValue(objectType, map);
    }

    @Override
    public boolean isIterable() {
        return true;
    }

    @Override
    public Type iteratorType(ObjectType objectType) {
        return MapEntryClass.getQgClass().getObjectTypeFromTypeArgs(Arrays.asList(
                objectType.getInnerTypes().get(0),
                objectType.getInnerTypes().get(1)));
    }

    @Override
    public boolean isComparable() {
        return false;
    }

    @Override
    public Type getObjectTypeFromTypeArgs(List<Type> typeArguments) {
        return new ObjectType(this, typeArguments.get(0), typeArguments.get(1));
    }

    public class HashMapValue extends ObjectValue {

        HashMap<Object, Object> map;

        public HashMapValue(ObjectType objectType, HashMap<Object, Object> map) {
            super(NAME, objectType);
            this.map = map;
        }

        @Override
        public Object execute(int methodId, List<Expression> arguments, Program program) {
            return HashMapClass.this.execute(this, methodId, arguments, program);
        }

        @Override
        public Iterator<Object> iterator() {
            return new QgMapIterator(map.entrySet().iterator(), getType().getInnerTypes().get(0), getType().getInnerTypes().get(1));
        }

        public double size() {
            return map.size();
        }

        public Object get(Object key) {
            return map.get(key);
        }

        public Object put(Object key, Object value) {
            map.put(key, value);
            return null;
        }

        public boolean containsKey(Object key) {
            return map.containsKey(key);
        }

        public Object getOrDefault(Object key, Object defaultValue) {
            return map.getOrDefault(key, defaultValue);
        }

        public boolean removeKey(Object key) {
            return map.remove(key) != null;
        }

        public boolean remove(Object key, Object value) {
            return map.remove(key, value);
        }

        public ListClass.ListObject values() {
            return ListClass.getQgClass().createInstance(getType().getInnerTypes().get(0), new ArrayList<>(map.values()));
        }

        public HashSetClass.HashSetObject keys() {
            return HashSetClass.getQgClass().createInstance(getType().getInnerTypes().get(0), new HashSet<>(map.keySet()));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HashMapValue that = (HashMapValue) o;
            return Objects.equals(map, that.map);
        }

        @Override
        public int hashCode() {
            return map.hashCode();
        }
    }
}
