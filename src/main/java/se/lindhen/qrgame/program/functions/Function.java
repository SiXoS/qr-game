package se.lindhen.qrgame.program.functions;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.parser.Validator;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class Function {

    private final String name;

    public Function(String name) {
        this.name = name;
    }

    abstract public Type getReturnType(ArrayList<Expression> arguments);
    public abstract Object execute(ArrayList<Expression> arguments, Program program);
    public abstract ValidationResult validate(ArrayList<Expression> arguments, ParserRuleContext ctx);
    public abstract Optional<Integer> getConstantParameterCount();
    public abstract boolean isConstant();

    public String getName() {
        return name;
    }

    protected ValidationResult validateArguments(ArrayList<Expression> arguments, ParserRuleContext ctx, Type... types) {
        return validateArguments(this.name, arguments, Arrays.asList(types), ctx);
    }

    public static ValidationResult validateArguments(String functionName, ArrayList<Expression> arguments, List<Type> types, ParserRuleContext ctx) {
        if (arguments.size() != types.size()) {
            return ValidationResult.invalid(ctx, "Function " + functionName + " expected " + types.size() + " arguments but got " + arguments.size());
        } else {
            ArrayList<ValidationResult> argumentResults = new ArrayList<>();
            for (int i = 0; i < types.size(); i++) {
                if (!arguments.get(i).getType().equals(types.get(i))) {
                    argumentResults.add(ValidationResult.invalid(ctx, "Argument " + i + " to function " + functionName + " was " + arguments.get(i).getType() + " but expected " + types.get(i)));
                } else {
                    argumentResults.add(ValidationResult.valid());
                }
            }
            return ValidationResult.multiple(argumentResults);
        }
    }
}
