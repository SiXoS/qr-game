package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.functions.FunctionDeclaration;
import se.lindhen.qrgame.program.objects.utils.*;
import se.lindhen.qrgame.program.types.*;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.*;

public class TreeMapClass extends QgClass<TreeMapClass.TreeMapValue> {

    private static final String NAME = "TreeMap";
    private static final TreeMapClass instance = new TreeMapClass();

    private TreeMapClass() {
        super(NAME);
    }

    public static TreeMapClass getQgClass() {
        return instance;
    }

    @Override
    public ArgumentCountValidation validateArgumentCount(int arguments) {
        return ArgumentCountValidation.validate(2, arguments);
    }

    @Override
    protected List<Method<TreeMapValue>> getMethods() {
        ArrayList<Method<TreeMapValue>> methods = new ArrayList<>();
        ObjectType objectType = new ObjectType(this, new GenericType(0), new GenericType(1));
        methods.add(new LambdaMethod<>( "size", (obj, args, prog) -> obj.size(), new FunctionDeclaration(2, NumberType.NUMBER_TYPE, objectType)));
        methods.add(new LambdaMethod<>( "get", (obj, args, prog) -> obj.get(args.get(0).calculate(prog)), new FunctionDeclaration(2, new GenericType(1), objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>( "getOrDefault", (obj, args, prog) -> obj.getOrDefault(args.get(0).calculate(prog), args.get(1).calculate(prog)), new FunctionDeclaration(2, new GenericType(1), objectType, new GenericType(0),new GenericType(1))));
        methods.add(new LambdaMethod<>( "put", (obj, args, prog) -> obj.put(args.get(0).calculate(prog), args.get(1).calculate(prog)), new FunctionDeclaration(2, new GenericType(1), objectType, new GenericType(0),new GenericType(1))));
        methods.add(new LambdaMethod<>( "containsKey", (obj, args, prog) -> obj.containsKey(args.get(0).calculate(prog)), new FunctionDeclaration(2, BoolType.BOOL_TYPE, objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>( "removeKey", (obj, args, prog) -> obj.removeKey(args.get(0).calculate(prog)), new FunctionDeclaration(2, new GenericType(1), objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>( "remove", (obj, args, prog) -> obj.remove(args.get(0).calculate(prog), args.get(1).calculate(prog)), new FunctionDeclaration(2, BoolType.BOOL_TYPE, objectType, new GenericType(0),new GenericType(1))));
        methods.add(new LambdaMethod<>( "nextKey", (obj, args, prog) -> obj.nextKey(args.get(0).calculate(prog)), new FunctionDeclaration(2, new GenericType(0), objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>( "previousKey", (obj, args, prog) -> obj.previousKey(args.get(0).calculate(prog)), new FunctionDeclaration(2, new GenericType(0), objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>( "nextEntry", (obj, args, prog) -> obj.nextEntry(args.get(0).calculate(prog)), new FunctionDeclaration(2, entryType(), objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>( "previousEntry", (obj, args, prog) -> obj.previousEntry(args.get(0).calculate(prog)), new FunctionDeclaration(2, entryType(), objectType, new GenericType(0))));
        methods.add(new LambdaMethod<>( "firstKey", (obj, args, prog) -> obj.firstKey(), new FunctionDeclaration(2, new GenericType(0), objectType)));
        methods.add(new LambdaMethod<>( "lastKey", (obj, args, prog) -> obj.lastKey(), new FunctionDeclaration(2, new GenericType(0), objectType)));
        methods.add(new LambdaMethod<>( "firstEntry", (obj, args, prog) -> obj.firstEntry(), new FunctionDeclaration(2, entryType(), objectType)));
        methods.add(new LambdaMethod<>( "lastEntry", (obj, args, prog) -> obj.lastEntry(), new FunctionDeclaration(2, entryType(), objectType)));
        methods.add(new LambdaMethod<>( "values", (obj, args, prog) -> obj.values(), new FunctionDeclaration(2, TypeUtils.listWithGenericType(1), objectType)));
        methods.add(new LambdaMethod<>( "keys", (obj, args, prog) -> obj.keys(), new FunctionDeclaration(2, TypeUtils.setWithGenericType(TreeSetClass.getQgClass(), 0), objectType)));
        methods.add(new ForEachMapEntryMethod<>("putAll", TreeMapValue::put, objectType));
        methods.add(new ForEachMapEntryMethod<>("removeAll", TreeMapValue::remove, objectType));
        methods.add(new ForEachMethod<>("removeAllKeys", TreeMapValue::removeKey, objectType, 2, 0));
        return methods;
    }

    public TreeMapValue createInstance(TreeMap<Object, Object> map) {
        return new TreeMapValue(map);
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

    private static Type entryType() {
        return MapEntryClass.getQgClass().getObjectTypeFromTypeArgs(Arrays.asList(new GenericType(0), new GenericType(1)));
    }

    public class TreeMapValue extends ObjectValue {

        TreeMap<Object, Object> map;

        private TreeMapValue(TreeMap<Object, Object> map) {
            super(NAME);
            this.map = map;
        }

        @Override
        public Object execute(int methodId, List<Expression> arguments, Program program) {
            return TreeMapClass.this.execute(this, methodId, arguments, program);
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
            return MapEntryClass.getQgClass().createInstance(entry.getKey(), entry.getValue());
        }

        public boolean remove(Object key, Object value) {
            return map.remove(key, value);
        }

        public ListClass.ListObject values() {
            return ListClass.getQgClass().createInstance(new ArrayList<>(map.values()));
        }

        public TreeSetClass.TreeSetObject keys() {
            return TreeSetClass.getQgClass().createInstance(new TreeSet<>(map.keySet()));
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
