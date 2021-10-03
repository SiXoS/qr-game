package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.utils.GenericInnerType;
import se.lindhen.qrgame.program.objects.utils.LambdaMethod;
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
    protected List<LambdaMethod<MapEntryObject, GenericType>> getMethods() {
        ArrayList<LambdaMethod<MapEntryObject, GenericType>> methods = new ArrayList<>();
        methods.add(new LambdaMethod<>(new GenericInnerType(0), "getKey", (obj, args, prog) -> obj.getKey()));
        methods.add(new LambdaMethod<>(new GenericInnerType(1), "getValue", (obj, args, prog) -> obj.getValue()));
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

    public Type getObjectType(ArrayList<Expression> arguments) {
        return new ObjectType(this, arguments.get(0).getType(), arguments.get(1).getType());
    }

    @Override
    public boolean isComparable() {
        return false;
    }

    public MapEntryObject createInstance(Object key, Object value) {
        return new MapEntryObject(key, value);
    }

    public class MapEntryObject extends ObjectValue {

        private final Object key;
        private final Object value;

        public MapEntryObject(Object key, Object value) {
            super(NAME);
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
