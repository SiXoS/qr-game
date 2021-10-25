package se.lindhen.qrgame.program.functions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.drawings.Shape;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.objects.ListClass;
import se.lindhen.qrgame.program.objects.ShapeClass;
import se.lindhen.qrgame.program.types.FunctionType;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.ObjectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CompositeShapeFunction extends Function {

    public static final String NAME = "createCompositeShape";

    public CompositeShapeFunction() {
        super(NAME, new FunctionType(new ObjectType(ShapeClass.getQgClass()),
                NumberType.NUMBER_TYPE,
                NumberType.NUMBER_TYPE,
                ListClass.getQgClass().getObjectTypeFromTypeArgs(Collections.singletonList(ShapeClass.getQgClass().getObjectType()))));
    }

    @Override
    public Object execute(List<Expression> arguments, Program program) {
        double x = (double) arguments.get(0).calculate(program);
        double y = (double) arguments.get(1).calculate(program);
        ListClass.ListObject shapes = (ListClass.ListObject) arguments.get(2).calculate(program);
        return ShapeClass.getQgClass().createInstance(program.getShapeFactory().createComposite(x, y, fromObjects(shapes.getBackingList())));
    }

    private List<Shape> fromObjects(List<Object> objects) {
        ArrayList<Shape> shapes = new ArrayList<>(objects.size());
        for (Object object : objects) {
            shapes.add(((ShapeClass.ShapeObject) object).getShape());
        }
        return shapes;
    }

    @Override
    public Optional<Integer> getConstantParameterCount() {
        return Optional.of(3);
    }

    @Override
    public boolean isConstant() {
        return false;
    }
}
