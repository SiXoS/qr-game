package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.utils.*;
import se.lindhen.qrgame.program.types.*;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.GenericType;

import java.util.*;

import static se.lindhen.qrgame.program.objects.utils.TypeUtils.listWithGenericType;
import static se.lindhen.qrgame.program.objects.utils.TypeUtils.setWithGenericType;

public class HashMapClass extends QgClass<HashMapClass.HashMapValue> {

    private static final String NAME = "HashMap";
    private static final HashMapClass instance = new HashMapClass();

    private HashMapClass() {
        super(NAME);
    }

    public static HashMapClass getQgClass() {
        return instance;
    }

    @Override
    protected List<Method<HashMapValue>> getMethods() {
        ArrayList<Method<HashMapValue>> methods = new ArrayList<>();
        Type keyType = new GenericType(0);
        Type valueType = new GenericType(1);
        ObjectType objectType = new ObjectType(this, keyType, valueType);
        methods.add(new LambdaMethod<>("size", (obj, args, prog) -> obj.size(), new FunctionType(NumberType.NUMBER_TYPE, objectType)));
        methods.add(new LambdaMethod<>("get", (obj, args, prog) -> obj.get(args.get(0).calculate(prog)), new FunctionType(valueType, objectType, keyType)));
        methods.add(new LambdaMethod<>("getOrDefault", (obj, args, prog) -> obj.getOrDefault(args.get(0).calculate(prog), args.get(1).calculate(prog)), new FunctionType(valueType, objectType, keyType, valueType)));
        methods.add(new LambdaMethod<>("put", (obj, args, prog) -> obj.put(args.get(0).calculate(prog), args.get(1).calculate(prog)), new FunctionType(valueType, objectType, keyType, valueType)));
        methods.add(new LambdaMethod<>("containsKey", (obj, args, prog) -> obj.containsKey(args.get(0).calculate(prog)), new FunctionType(BoolType.BOOL_TYPE, objectType, keyType)));
        methods.add(new LambdaMethod<>("removeKey", (obj, args, prog) -> obj.removeKey(args.get(0).calculate(prog)), new FunctionType(valueType, objectType, keyType)));
        methods.add(new LambdaMethod<>("remove", (obj, args, prog) -> obj.remove(args.get(0).calculate(prog), args.get(1).calculate(prog)), new FunctionType(BoolType.BOOL_TYPE, objectType, keyType, valueType)));
        methods.add(new LambdaMethod<>("values", (obj, args, prog) -> obj.values(), new FunctionType(listWithGenericType(1), objectType)));
        methods.add(new LambdaMethod<>("keys", (obj, args, prog) -> obj.keys(), new FunctionType(setWithGenericType(HashSetClass.getQgClass(), 0), objectType)));
        methods.add(new ForEachMapEntryMethod<>("putAll", HashMapValue::put, objectType));
        methods.add(new ForEachMapEntryMethod<>("removeAll", HashMapValue::remove, objectType));
        methods.add(new ForEachMethod<>("removeAllKeys", HashMapValue::removeKey, objectType, 0));
        return methods;
    }

    @Override
    public ArgumentCountValidation validateArgumentCount(int arguments) {
        return ArgumentCountValidation.validate(2, arguments);
    }

    public HashMapValue createInstance(HashMap<Object, Object> map) {
        return new HashMapValue(map);
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

        public HashMapValue(HashMap<Object, Object> map) {
            super(NAME);
            this.map = map;
        }

        @Override
        public Object execute(int methodId, List<Expression> arguments, Program program) {
            return HashMapClass.this.execute(this, methodId, arguments, program);
        }

        @Override
        public Iterator<Object> iterator() {
            return new QgMapIterator(map.entrySet().iterator());
        }

        public double size() {
            return map.size();
        }

        public Object get(Object key) {
            return map.get(key);
        }

        public Object put(Object key, Object value) {
            return map.put(key, value);
        }

        public boolean containsKey(Object key) {
            return map.containsKey(key);
        }

        public Object getOrDefault(Object key, Object defaultValue) {
            return map.getOrDefault(key, defaultValue);
        }

        public Object removeKey(Object key) {
            return map.remove(key);
        }

        public boolean remove(Object key, Object value) {
            return map.remove(key, value);
        }

        public ListClass.ListObject values() {
            return ListClass.getQgClass().createInstance(new ArrayList<>(map.values()));
        }

        public HashSetClass.HashSetObject keys() {
            return HashSetClass.getQgClass().createInstance(new HashSet<>(map.keySet()));
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
