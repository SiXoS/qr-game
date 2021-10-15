package se.lindhen.qrgame.program.objects;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.functions.FunctionDeclaration;
import se.lindhen.qrgame.program.objects.utils.LambdaMethod;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.ObjectType;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.drawings.Shape;
import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.VoidType;

import java.util.ArrayList;
import java.util.List;

public class ShapeClass extends QgClass<ShapeClass.ShapeObject> {

    public static final String NAME = "Shape";
    private static final ShapeClass qgClass = new ShapeClass();
    private final ObjectType objectType = new ObjectType(this);

    private ShapeClass() {
        super(NAME);
    }

    @Override
    public ArgumentCountValidation validateArgumentCount(int arguments) {
        return ArgumentCountValidation.validate(0, arguments);
    }

    public static ShapeClass getQgClass() {
        return qgClass;
    }

    public ShapeObject createInstance(Shape shape) {
        return new ShapeObject(shape);
    }

    public Type getObjectType() {
        return objectType;
    }

    @Override
    protected List<Method<ShapeObject>> getMethods() {
        ArrayList<Method<ShapeObject>> methods = new ArrayList<>();
        methods.add(new SetShapeParamFunction("setPosX", Shape::setPosX));
        methods.add(new SetShapeParamFunction("setPosY", Shape::setPosY));
        methods.add(new SetShapeParamFunction("setScaleX", Shape::setScaleX));
        methods.add(new SetShapeParamFunction("setScaleY", Shape::setScaleY));
        methods.add(new SetShapeParamFunction("setRotationDeg", Shape::setRotationDeg));
        methods.add(new SetShapeParamFunction("setSpeedPerSecondX", Shape::setSpeedPerSecondX));
        methods.add(new SetShapeParamFunction("setSpeedPerSecondY", Shape::setSpeedPerSecondY));
        methods.add(new SetShapeParamFunction("setAccelerationPerSecondX", Shape::setAccelerationPerSecondX));
        methods.add(new SetShapeParamFunction("setAccelerationPerSecondY", Shape::setAccelerationPerSecondY));
        methods.add(new SetShapeParamFunction("setRotationDegSpeedPerSecond", Shape::setRotationDegSpeedPerSecond));
        methods.add(new GetShapeParamFunction("getPosX", Shape::getPosX));
        methods.add(new GetShapeParamFunction("getPosY", Shape::getPosY));
        methods.add(new GetShapeParamFunction("getScaleX", Shape::getScaleX));
        methods.add(new GetShapeParamFunction("getScaleY", Shape::getScaleY));
        methods.add(new GetShapeParamFunction("getRotationDeg", Shape::getRotationDeg));
        methods.add(new GetShapeParamFunction("getSpeedPerSecondX", Shape::getSpeedPerSecondX));
        methods.add(new GetShapeParamFunction("getSpeedPerSecondY", Shape::getSpeedPerSecondY));
        methods.add(new GetShapeParamFunction("getAccelerationPerSecondX", Shape::getAccelerationPerSecondX));
        methods.add(new GetShapeParamFunction("getAccelerationPerSecondY", Shape::getAccelerationPerSecondY));
        methods.add(new GetShapeParamFunction("getRotationDegSpeedPerSecond", Shape::getRotationDegSpeedPerSecond));
        ObjectType shapeType = new ObjectType(ShapeClass.this);
        methods.add(new LambdaMethod<>("setColorBackground", (shape, args, prog) -> shape.setColorBackground(), new FunctionDeclaration(0, shapeType, shapeType)));
        methods.add(new LambdaMethod<>("setColorForeground", (shape, args, prog) -> shape.setColorForeground(), new FunctionDeclaration(0, shapeType, shapeType)));
        methods.add(new LambdaMethod<>("addChild", (shape, args, prog) -> shape.addChild((ShapeObject) args.get(0).calculate(prog)), new FunctionDeclaration(0, shapeType, shapeType, shapeType)));
        methods.add(new LambdaMethod<>("update", (shape, args, prog) -> shape.update(prog.getSecondsDeltaTime()), new FunctionDeclaration(0, VoidType.VOID_TYPE, shapeType)));
        return methods;
    }

    @Override
    public boolean isIterable() {
        return false;
    }

    @Override
    public Type getObjectTypeFromTypeArgs(List<Type> typeArguments) {
        return new ObjectType(this);
    }

    @Override
    public boolean isComparable() {
        return false;
    }

    private class SetShapeParamFunction extends LambdaMethod<ShapeObject> {
        public SetShapeParamFunction(String name, ShapeParamConsumer function) {
            super(
                    name,
                    (shape, args, vars) -> {
                        Object value = args.get(0).calculate(vars);
                        function.apply(shape.getShape(), (double) value);
                        return shape;
                    },
                    new FunctionDeclaration(0, new ObjectType(ShapeClass.this), new ObjectType(ShapeClass.this), NumberType.NUMBER_TYPE));
        }
    }

    private interface ShapeParamConsumer {
        void apply(Shape shape, double value);
    }

    private class GetShapeParamFunction extends LambdaMethod<ShapeObject> {
        public GetShapeParamFunction(String name, ShapeParamProducer function) {
            super(
                    name,
                    (shape, args, vars) -> function.apply(shape.getShape()),
                    new FunctionDeclaration(0, NumberType.NUMBER_TYPE, new ObjectType(ShapeClass.this)));
        }
    }

    private interface ShapeParamProducer {
        double apply(Shape shape);
    }

    public class ShapeObject extends ObjectValue {
        private final Shape shape;

        private ShapeObject(Shape shape) {
            super(ShapeClass.NAME);
            this.shape = shape;
        }

        @Override
        public Object execute(int methodId, List<Expression> arguments, Program program) {
            return ShapeClass.this.execute(this, methodId, arguments, program);
        }

        public Shape getShape() {
            return shape;
        }

        public ShapeObject addChild(ShapeObject child) {
            shape.addChild(child.getShape());
            return this;
        }

        public Void update(double deltaTime) {
            shape.update(deltaTime);
            return null;
        }

        @Override
        public int hashCode() {
            return shape.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof ShapeObject)) {
                return false;
            }
            return shape.equals(((ShapeObject) obj).shape);
        }

        public ShapeObject setColorBackground() {
            shape.setColorBackground();
            return this;
        }

        public ShapeObject setColorForeground() {
            shape.setColorForeground();
            return this;
        }
    }

}
