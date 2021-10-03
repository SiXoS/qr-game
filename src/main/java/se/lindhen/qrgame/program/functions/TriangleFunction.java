package se.lindhen.qrgame.program.functions;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.objects.ShapeClass;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TriangleFunction extends Function {

    public static final String NAME = "createTriangle";

    public TriangleFunction() {
        super(NAME, new FunctionDeclaration(0, new ObjectType(ShapeClass.getQgClass()), NumberType.NUMBER_TYPE, NumberType.NUMBER_TYPE, NumberType.NUMBER_TYPE, NumberType.NUMBER_TYPE));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        double x = (double) arguments.get(0).calculate(program);
        double y = (double) arguments.get(1).calculate(program);
        double w = (double) arguments.get(2).calculate(program);
        double h = (double) arguments.get(3).calculate(program);
        return ShapeClass.getQgClass().createInstance(program.getShapeFactory().createTriangle(x, y, w, h));
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(4);
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
