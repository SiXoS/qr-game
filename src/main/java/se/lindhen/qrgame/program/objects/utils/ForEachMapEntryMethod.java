package se.lindhen.qrgame.program.objects.utils;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.MapEntryClass;
import se.lindhen.qrgame.program.objects.Method;
import se.lindhen.qrgame.program.objects.ObjectValue;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.VoidType;
import se.lindhen.qrgame.util.TriConsumer;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ForEachMapEntryMethod<O extends ObjectValue> extends Method<O> {

    private final TriConsumer<O, Object, Object> modifier;

    public ForEachMapEntryMethod(String name, TriConsumer<O, Object, Object> objectModifier) {
        super(name);
        this.modifier = objectModifier;
    }

    @Override
    public Object execute(O object, List<Expression> arguments, Program program) {
        ObjectValue toIterate = (ObjectValue) arguments.get(0).calculate(program);
        Iterator<Object> iterator = toIterate.iterator();
        while (iterator.hasNext()) {
            MapEntryClass.MapEntryObject entry = (MapEntryClass.MapEntryObject) iterator.next();
            modifier.apply(object, entry.getKey(), entry.getValue());
        }
        return null;
    }

    @Override
    public ValidationResult validate(ObjectType toBeModifiedObjectType, List<Expression> arguments, ParserRuleContext ctx) {
        if (arguments.size() != 1) {
            return ValidationResult.invalid(ctx, "Expected 1 argument, got '" + arguments.size() + "'");
        }
        if (!arguments.get(0).getType().isObject()) {
            return ValidationResult.invalid(ctx, "expected map, got '" + arguments.get(0).getType() + "'");
        }
        ObjectType toIterateType = (ObjectType) arguments.get(0).getType();
        Type iteratorType = toIterateType.getQgClass().iteratorType(toIterateType);
        if (iteratorType != null && iteratorType.isObject() && ((ObjectType) iteratorType).getQgClass().equals(MapEntryClass.getQgClass())) { // source iterates over map entries
            if (!getKeyType(iteratorType).canBeAssignedTo(getKeyType(toBeModifiedObjectType))) {
                return ValidationResult.invalid(ctx, "Key type does not match. Source type: '" + getKeyType(iteratorType) + "', target type: '" + getKeyType(toBeModifiedObjectType) + "'");
            } else if (!getValueType(iteratorType).canBeAssignedTo(getValueType(toBeModifiedObjectType))) {
                return ValidationResult.invalid(ctx, "Value type does not match. Source type: '" + getValueType(iteratorType) + "', target type: '" + getValueType(toBeModifiedObjectType) + "'");
            } else {
                return ValidationResult.valid();
            }
        } else {
            return ValidationResult.invalid(ctx, "argument to " + name + "  must be iterable over map entries. Type '" + toIterateType + "' is not.");
        }
    }

    private Type getKeyType(Type mapType) {
        return ((ObjectType) mapType).getInnerTypes().get(0);
    }

    private Type getValueType(Type mapType) {
        return ((ObjectType) mapType).getInnerTypes().get(1);
    }

    @Override
    public Type getReturnType(ObjectType objectType) {
        return VoidType.VOID_TYPE;
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(1);
    }
}
