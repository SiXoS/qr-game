package se.lindhen.qrgame.program.objects.utils;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.objects.MapEntryClass;
import se.lindhen.qrgame.program.objects.QgClass;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.TypeType;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class MapConstructionUtils {

    public static ObjectType typeFromArguments(QgClass<?> parent, List<Expression> arguments) {
        return arguments.get(0).getType().isType() ?
                new ObjectType(parent, ((TypeType) arguments.get(0).getType()).getActualType(), ((TypeType) arguments.get(1).getType()).getActualType()) :
                new ObjectType(parent, ((ObjectType) arguments.get(0).getType()).getInnerTypes().get(0), ((ObjectType) arguments.get(0).getType()).getInnerTypes().get(1));
    }

    public static <T extends Map<Object, Object>> T createMap(List<Expression> arguments, Program program, Supplier<T> mapCreator) {
        T map = mapCreator.get();
        if (!arguments.get(0).getType().isType()) {
            for (Expression argument : arguments) {
                MapEntryClass.MapEntryObject entry = (MapEntryClass.MapEntryObject) argument.calculate(program);
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }

    public static ValidationResult validate(List<Expression> arguments, ParserRuleContext ctx) {
        if (arguments.isEmpty()) {
            return ValidationResult.invalid(ctx, "Expected two types or some number of map entry values. Got no arguments.");
        }
        if (arguments.get(0).getType().isType()) {
            if (arguments.size() != 2) {
                return ValidationResult.invalid(ctx, "When creating map by types you may only specify 2 arguments, got '" + arguments.size() + "'");
            } else if (arguments.get(1).getType().isType()) {
                return ValidationResult.valid();
            } else {
                return ValidationResult.invalid(ctx, "First argument was type. Expected second argument to also be type, but was '" + arguments.get(1).getType());
            }
        } else {
            Type typeOfFirstArg = arguments.get(0).getType();
            for (int i = 0; i < arguments.size(); i++) {
                Type typeOfCurrentArg = arguments.get(i).getType();
                if (!typeOfCurrentArg.isObject() || ((ObjectType) typeOfCurrentArg).getQgClass() != MapEntryClass.getQgClass()) {
                    return ValidationResult.invalid(ctx, "Argument " + (i + 1) + " was type " + typeOfCurrentArg + ", expected a MapEntry.");
                }
                if (!typeOfFirstArg.equals(typeOfCurrentArg)) {
                    return ValidationResult.invalid(ctx, "All arguments must be the same type. First argument is '" + typeOfFirstArg + "' but argument " + (i+1) + " is '" + typeOfCurrentArg + "'");
                }
            }
            return ValidationResult.valid();
        }
    }

}
