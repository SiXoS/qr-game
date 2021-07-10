package se.lindhen.qrgame.program.drawings;

import java.util.List;

public class DefaultShapeFactory implements ShapeFactory {

    @Override
    public Shape createRect(double x, double y, double width, double height) {
        return new SimpleShape(x, y, width, height, Shape.Type.RECTANGLE);
    }

    @Override
    public Shape createEllipse(double x, double y, double width, double height) {
        return new SimpleShape(x, y, width, height, Shape.Type.ELLIPSE);
    }

    @Override
    public Shape createComposite(double x, double y, List<Shape> children) {
        return new CompositeShape(x, y, children);
    }

    @Override
    public Shape createTriangle(double x, double y, double width, double height) {
        return new SimpleShape(x, y, width, height, Shape.Type.TRIANGLE);
    }
}
