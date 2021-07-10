package se.lindhen.qrgame.program.drawings;

import java.util.List;

public class CompositeShape extends Shape {

    public CompositeShape(double x, double y, List<Shape> children) {
        super(x, y);
        this.children = children;
    }

    @Override
    public Type getType() {
        return Type.EMPTY;
    }

    @Override
    public void update(double secSinceLastFrame) {
        super.updatePosition(secSinceLastFrame);
        if (children != null) {
            for (Shape child : children) {
                child.update(secSinceLastFrame, this);
            }
        }
    }

    @Override
    public void update(double secSinceLastFrame, Shape parent) {
        update(secSinceLastFrame);
    }
}
