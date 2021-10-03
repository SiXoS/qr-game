package se.lindhen.qrgame.program.objects.utils;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.Method;
import se.lindhen.qrgame.program.objects.ObjectValue;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.VoidType;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class ForEachMethod<O extends ObjectValue> extends Method<O> {

    private final BiConsumer<O, Object> modifier;

    public ForEachMethod(String name, BiConsumer<O, Object> modifier) {
        super(name);
        this.modifier = modifier;
    }

    @Override
    public Object execute(O object, List<Expression> arguments, Program program) {
        ObjectValue toIterate = (ObjectValue) arguments.get(0).calculate(program);
        Iterator<Object> iterator = toIterate.iterator();
        while (iterator.hasNext()) {
            modifier.accept(object, iterator.next());
        }
        return null;
    }

    @Override
    public ValidationResult validate(ObjectType toModifyObjectType, List<Expression> arguments, ParserRuleContext ctx) {
        if (arguments.size() != 1) {
            return ValidationResult.invalid(ctx, "Expected 1 argument, got '" + arguments.size() + "'");
        }
        if (!arguments.get(0).getType().isObject()) {
            return ValidationResult.invalid(ctx, "expected collection, got '" + arguments.get(0).getType() + "'");
        }
        ObjectType sourceType = (ObjectType) arguments.get(0).getType();
        Type iteratorType = sourceType.getQgClass().iteratorType(sourceType);
        if (!iteratorType.canBeAssignedTo(toModifyObjectType.getInnerTypes().get(0))) {
            return ValidationResult.invalid(ctx, "Iterator type does not match. Source type: '" + iteratorType + "', target type: '" + toModifyObjectType.getInnerTypes().get(0) + "'");
        } else {
            return ValidationResult.valid();
        }
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
