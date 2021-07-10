package se.lindhen.qrgame.program.drawings;

import java.util.List;

public interface ShapeFactory {

    Shape createRect(double x, double y, double width, double height);

    Shape createEllipse(double x, double y, double width, double height);

    Shape createComposite(double x, double y, List<Shape> children);

    Shape createTriangle(double x, double y, double width, double height);

}
