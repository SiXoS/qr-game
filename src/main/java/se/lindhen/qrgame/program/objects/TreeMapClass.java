package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.utils.*;
import se.lindhen.qrgame.program.types.*;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.*;

public class TreeMapClass extends QgClass<TreeMapClass.TreeMapValue> {

    private static final String NAME = "TreeMap";
    private static final TreeMapClass instance = new TreeMapClass();

    private TreeMapClass() {
        super(NAME, 2);
    }

    public static TreeMapClass getQgClass() {
        return instance;
    }

    @Override
    protected List<Method<TreeMapValue>> getMethods() {
        ArrayList<Method<TreeMapValue>> methods = new ArrayList<>();
        methods.add(new LambdaMethod<>(new ConstantGenericType(NumberType.NUMBER_TYPE), "size", (obj, args, prog) -> obj.size()));
        methods.add(new LambdaMethod<>(new GenericInnerType(1), "get", (obj, args, prog) -> obj.get(args.get(0).calculate(prog)), new GenericInnerType(0)));
        methods.add(new LambdaMethod<>(new GenericInnerType(1), "getOrDefault", (obj, args, prog) -> obj.getOrDefault(args.get(0).calculate(prog), args.get(1).calculate(prog)), new GenericInnerType(0), new GenericInnerType(1)));
        methods.add(new LambdaMethod<>(new GenericInnerType(1), "put", (obj, args, prog) -> obj.put(args.get(0).calculate(prog), args.get(1).calculate(prog)), new GenericInnerType(0), new GenericInnerType(1)));
        methods.add(new LambdaMethod<>(new ConstantGenericType(BoolType.BOOL_TYPE), "containsKey", (obj, args, prog) -> obj.containsKey(args.get(0).calculate(prog)), new GenericInnerType(0)));
        methods.add(new LambdaMethod<>(new GenericInnerType(1), "removeKey", (obj, args, prog) -> obj.removeKey(args.get(0).calculate(prog)), new GenericInnerType(0)));
        methods.add(new LambdaMethod<>(new ConstantGenericType(BoolType.BOOL_TYPE), "remove", (obj, args, prog) -> obj.remove(args.get(0).calculate(prog), args.get(1).calculate(prog)), new GenericInnerType(0), new GenericInnerType(1)));
        methods.add(new LambdaMethod<>(new GenericInnerType(0), "nextKey", (obj, args, prog) -> obj.nextKey(args.get(0).calculate(prog)), new GenericInnerType(0)));
        methods.add(new LambdaMethod<>(new GenericInnerType(0), "previousKey", (obj, args, prog) -> obj.previousKey(args.get(0).calculate(prog)), new GenericInnerType(0)));
        methods.add(new LambdaMethod<>(new GenericEntryType(), "nextEntry", (obj, args, prog) -> obj.nextEntry(args.get(0).calculate(prog)), new GenericInnerType(0)));
        methods.add(new LambdaMethod<>(new GenericEntryType(), "previousEntry", (obj, args, prog) -> obj.previousEntry(args.get(0).calculate(prog)), new GenericInnerType(0)));
        methods.add(new LambdaMethod<>(new GenericInnerType(0), "firstKey", (obj, args, prog) -> obj.firstKey()));
        methods.add(new LambdaMethod<>(new GenericInnerType(0), "lastKey", (obj, args, prog) -> obj.lastKey()));
        methods.add(new LambdaMethod<>(new GenericEntryType(), "firstEntry", (obj, args, prog) -> obj.firstEntry()));
        methods.add(new LambdaMethod<>(new GenericEntryType(), "lastEntry", (obj, args, prog) -> obj.lastEntry()));
        methods.add(new LambdaMethod<>(new ListWithValueTypeAsInnerType(), "values", (obj, args, prog) -> obj.values()));
        methods.add(new LambdaMethod<>(new SetWithKeyTypeAsInnerType(TreeSetClass.getQgClass()), "keys", (obj, args, prog) -> obj.keys()));
        methods.add(new ForEachMapEntryMethod<>("putAll", TreeMapValue::put));
        methods.add(new ForEachMapEntryMethod<>("removeAll", TreeMapValue::remove));
        methods.add(new ForEachMethod<>("removeAllKeys", TreeMapValue::removeKey));
        return methods;
    }

    public TreeMapValue createInstance(ObjectType objectType, TreeMap<Object, Object> map) {
        return new TreeMapValue(objectType, map);
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

    private static class GenericEntryType implements GenericType {

        @Override
        public Type getType(ObjectType objectType) {
            return MapEntryClass.getQgClass().getObjectTypeFromTypeArgs(Arrays.asList(objectType.getInnerTypes().get(0), objectType.getInnerTypes().get(1)));
        }

    }

    public class TreeMapValue extends ObjectValue {

        TreeMap<Object, Object> map;

        private TreeMapValue(ObjectType objectType, TreeMap<Object, Object> map) {
            super(NAME, objectType);
            this.map = map;
        }

        @Override
        public Object execute(int methodId, List<Expression> arguments, Program program) {
            return TreeMapClass.this.execute(this, methodId, arguments, program);
        }

        @Override
        public Iterator<Object> iterator() {
            return new QgMapIterator(map.entrySet().iterator(), getKeyType(), getValueType());
        }

        private Type getKeyType() {
            return getType().getInnerTypes().get(0);
        }

        private Type getValueType() {
            return getType().getInnerTypes().get(1);
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

        public Object nextKey(Object key) {
            return map.higherKey(key);
        }

        public MapEntryClass.MapEntryObject nextEntry(Object key) {
            return createQgMapEntry(map.higherEntry(key));
        }

        public Object previousKey(Object key) {
            return map.lowerKey(key);
        }

        public MapEntryClass.MapEntryObject previousEntry(Object key) {
            return createQgMapEntry(map.lowerEntry(key));
        }

        public Object firstKey() {
            if (map.isEmpty()) return null;
            return map.firstKey();
        }

        public Object lastKey() {
            if (map.isEmpty()) return null;
            return map.lastKey();
        }

        public MapEntryClass.MapEntryObject firstEntry() {
            if (map.isEmpty()) return null;
            return createQgMapEntry(map.firstEntry());
        }

        public MapEntryClass.MapEntryObject lastEntry() {
            if (map.isEmpty()) return null;
            return createQgMapEntry(map.lastEntry());
        }

        private MapEntryClass.MapEntryObject createQgMapEntry(Map.Entry<Object, Object> entry) {
            return MapEntryClass.getQgClass().createInstance(entry.getKey(), getKeyType(), entry.getValue(), getValueType());
        }

        public boolean remove(Object key, Object value) {
            return map.remove(key, value);
        }

        public ListClass.ListObject values() {
            return ListClass.getQgClass().createInstance(getValueType(), new ArrayList<>(map.values()));
        }

        public TreeSetClass.TreeSetObject keys() {
            return TreeSetClass.getQgClass().createInstance(getKeyType(), new TreeSet<>(map.keySet()));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TreeMapValue that = (TreeMapValue) o;
            return Objects.equals(map, that.map);
        }

        @Override
        public int hashCode() {
            return map.hashCode();
        }
    }

}
