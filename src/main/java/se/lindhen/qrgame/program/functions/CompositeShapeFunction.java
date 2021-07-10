package se.lindhen.qrgame.program.functions;

import org.antlr.v4.runtime.ParserRuleContext;
import se.lindhen.qrgame.parser.ValidationResult;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.drawings.Shape;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.objects.ListClass;
import se.lindhen.qrgame.program.objects.ShapeClass;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CompositeShapeFunction extends Function {

    public static final String NAME = "createCompositeShape";

    public CompositeShapeFunction() {
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
        ListClass.ListObject shapes = (ListClass.ListObject) arguments.get(2).calculate(program);
        return ShapeClass.getQgClass().createInstance(program.getShapeFactory().createComposite(x, y, fromObjects(shapes.getBackingList())));
    }

    private List<Shape> fromObjects(List<Object> objects) {
        ArrayList<Shape> shapes = new ArrayList<>(objects.size());
        for (Object object : objects) {
            shapes.add((Shape) object);
        }
        return shapes;
    }

    @Override
    public ValidationResult validate(ArrayList<Expression> arguments, ParserRuleContext ctx) {
        return validateArguments(arguments, ctx,
                NumberType.NUMBER_TYPE,
                NumberType.NUMBER_TYPE,
                ListClass.getQgClass().getObjectTypeFromTypeArgs(Collections.singletonList(ShapeClass.getQgClass().getObjectType())));
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(3);
    }
}
