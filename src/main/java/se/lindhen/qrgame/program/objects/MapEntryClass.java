package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.utils.LambdaMethod;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.program.types.GenericType;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapEntryClass extends QgClass<MapEntryClass.MapEntryObject> {

    public static final String NAME = "MapEntry";
    private static final MapEntryClass instance = new MapEntryClass();

    protected MapEntryClass() {
        super(NAME);
    }

    @Override
    protected List<LambdaMethod<MapEntryObject>> getMethods() {
        ArrayList<LambdaMethod<MapEntryObject>> methods = new ArrayList<>();
        ObjectType objectType = new ObjectType(this, new GenericType(0), new GenericType(1));
        methods.add(new LambdaMethod<>("getKey", (obj, args, prog) -> obj.getKey(), new FunctionType(new GenericType(0), objectType)));
        methods.add(new LambdaMethod<>("getValue", (obj, args, prog) -> obj.getValue(), new FunctionType(new GenericType(1), objectType)));
        return methods;
    }

    @Override
    public ArgumentCountValidation validateArgumentCount(int arguments) {
        return ArgumentCountValidation.validate(2, arguments);
    }

    public static MapEntryClass getQgClass() {
        return instance;
    }

    @Override
    public boolean isIterable() {
        return false;
    }

    @Override
    public Type getObjectTypeFromTypeArgs(List<Type> typeArguments) {
        return new ObjectType(this, typeArguments.get(0), typeArguments.get(1));
    }

    public MapEntryObject createInstance(Object key, Object value) {
        return new MapEntryObject(key, value);
    }

    public class MapEntryObject extends ObjectValue {

        private final Object key;
        private final Object value;

        public MapEntryObject(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Object execute(int methodId, List<Expression> arguments, Program program) {
            return MapEntryClass.this.execute(this, methodId, arguments, program);
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MapEntryObject that = (MapEntryObject) o;
            return Objects.equals(key, that.key) && Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}
