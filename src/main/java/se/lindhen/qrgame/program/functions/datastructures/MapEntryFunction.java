package se.lindhen.qrgame.program.functions.datastructures;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.program.types.GenericType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.functions.Function;
import se.lindhen.qrgame.program.objects.MapEntryClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MapEntryFunction extends Function {

    private static final String NAME = "mapEntry";

    public MapEntryFunction() {
        super(NAME, new FunctionType(
                MapEntryClass.getQgClass().getObjectTypeFromTypeArgs(Arrays.asList(new GenericType(0), new GenericType(1))),
                new GenericType(0),
                new GenericType(1)));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        return MapEntryClass.getQgClass().createInstance(arguments.get(0).calculate(program), arguments.get(1).calculate(program));
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
