package se.lindhen.qrgame.program.objects;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.parser.Validator;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Method<O extends ObjectValue> {

    protected final String name;

    protected Method(String name) {
        this.name = name;
    }

    public abstract Object execute(O object, List<Expression> arguments, Program program);

    public abstract ValidationResult validate(ObjectType objectType, List<Expression> arguments, ParserRuleContext ctx);

    public abstract Type getReturnType(ObjectType objectType);

    public abstract Optional<Integer> getConstantParameterCount();

    protected ValidationResult validateArguments(List<Expression> arguments, ParserRuleContext ctx, List<Type> types) {
        if (arguments.size() != types.size()) {
            return ValidationResult.invalid(ctx, "Method " + name + " expected " + types.size() + " arguments but got " + arguments.size());
        } else {
            ArrayList<ValidationResult> argumentResults = new ArrayList<>();
            for (int i = 0; i < types.size(); i++) {
                if (!arguments.get(i).getType().acceptsType(types.get(i))) {
                    argumentResults.add(ValidationResult.invalid(ctx, "Argument " + i + " to function " + name + " was " + arguments.get(i).getType() + " but expected " + types.get(i)));
                } else {
                    argumentResults.add(ValidationResult.valid());
                }
            }
            return ValidationResult.multiple(argumentResults);
        }
    }

}
