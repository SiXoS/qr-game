package se.lindhen.qrgame.program.functions.datastructures;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.objects.MapEntryClass;

import java.util.ArrayList;
import java.util.Optional;

public class MapEntryFunction extends Function {

    private static final String NAME = "mapEntry";

    public MapEntryFunction() {
        super(NAME);
    }

    @Override
    public Type getReturnType(ArrayList<Expression> arguments) {
        return MapEntryClass.getQgClass().getObjectType(arguments);
    }

    @Override
    public Object execute(ArrayList<Expression> arguments, Program program) {
        return MapEntryClass.getQgClass().createInstance(arguments.get(0).calculate(program), arguments.get(0).getType(), arguments.get(1).calculate(program), arguments.get(1).getType());
    }

    @Override
    public ValidationResult validate(ArrayList<Expression> arguments, ParserRuleContext ctx) {
        return arguments.size() == 2 ? ValidationResult.valid() : ValidationResult.invalid(ctx, "Expected 2 arguments but got '" + arguments.size() +"'");
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.empty();
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
