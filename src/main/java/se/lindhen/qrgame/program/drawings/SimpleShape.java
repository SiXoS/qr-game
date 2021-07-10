package se.lindhen.qrgame.program.drawings;

public class SimpleShape extends Shape {

    // Should remain final. Changing shape can be done by scaling.
    public final double width;
    public final double height;
    private final Type type;

    public SimpleShape(double x, double y, double width, double height, Type shapeType) {
        super(x,y);
        this.width = width;
        this.height = height;
        this.type = shapeType;
    }

    @Override
    public Type getType() {
        return type;
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

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
