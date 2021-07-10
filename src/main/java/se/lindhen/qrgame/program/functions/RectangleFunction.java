package se.lindhen.qrgame.program.functions;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.objects.ShapeClass;

import java.util.ArrayList;
import java.util.Optional;

public class RectangleFunction extends Function {

    public static final String NAME = "createRectangle";

    public RectangleFunction() {
        super(NAME);
    }

    @Override
    public Type getReturnType(ArrayList<Expression> arguments) {
        return new ObjectType(ShapeClass.getQgClass());
    }

    @Override
    public Object execute(ArrayList<Expression> arguments, Program program) {
        double x = (double) arguments.get(0).calculate(program);
        double y = (double) arguments.get(1).calculate(program);
        double w = (double) arguments.get(2).calculate(program);
        double h = (double) arguments.get(3).calculate(program);
        return ShapeClass.getQgClass().createInstance(program.getShapeFactory().createRect(x, y, w, h));
    }

    @Override
    public ValidationResult validate(ArrayList<Expression> arguments, ParserRuleContext ctx) {
        return validateArguments(arguments, ctx, NumberType.NUMBER_TYPE, NumberType.NUMBER_TYPE, NumberType.NUMBER_TYPE, NumberType.NUMBER_TYPE);
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(4);
    }
}
