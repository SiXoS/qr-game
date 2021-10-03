package se.lindhen.qrgame.program.objects.utils;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.TypeType;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class CollectionConstructionUtils {

    private CollectionConstructionUtils() {}

    public static Type innerTypeFromArguments(List<Type> arguments) {
        Type typeOfFirstArg = arguments.get(0);
        return typeOfFirstArg.isType() ? ((TypeType) typeOfFirstArg).getActualType() : typeOfFirstArg;
    }

    public static <T extends Collection<Object>> T createCollection(List<Expression> arguments, Program program, Function<Integer, T> collectionCreator) {
        T backingCollection;
        if (arguments.get(0).getType().isType()) {
            if (arguments.size() == 2) {
                backingCollection = collectionCreator.apply((int)(double)arguments.get(1).calculate(program));
            } else {
                backingCollection = collectionCreator.apply(10);
            }
        } else {
            backingCollection = collectionCreator.apply(arguments.size());
            for (Expression argument : arguments) {
                backingCollection.add(argument.calculate(program));
            }
        }
        return backingCollection;
    }

    public static ValidationResult validateCollection(List<Type> arguments, ParserRuleContext ctx) {
        if (arguments.size() == 0) {
            return ValidationResult.invalid(ctx, "At least a type is required, got 0 arguments.");
        }
        if (arguments.get(0).isType()) {
            if (arguments.size() == 1) {
                return ValidationResult.valid();
            } else if (arguments.size() == 2) {
                if (arguments.get(1).isNumber()) {
                    return ValidationResult.valid();
                } else {
                    return ValidationResult.invalid(ctx, "Second parameter to a typed collection constructor can only be an integer denoting the size. Got '" + arguments.get(1));
                }
            } else {
                return ValidationResult.invalid(ctx, "Typed collection constructor can only accept 1 or 2 arguments. Got '" + arguments.size());
            }
        }
        Type firstArgType = arguments.get(0);
        for (int i = 0; i < arguments.size(); i++) {
            Type argType = arguments.get(i);
            if (!argType.equals(firstArgType)) {
                return ValidationResult.invalid(ctx, "First argument had type '" + firstArgType + "' but argument " + (i + 1) + " had type '" + argType + "'");
            }
        }
        return ValidationResult.valid();
    }

}
