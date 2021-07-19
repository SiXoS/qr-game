package se.lindhen.qrgame.program.drawings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Shape {

    private static int id_counter = 0;
    private final int id = id_counter++;
    private double posX;
    private double posY;
    private double scaleX = 1;
    private double scaleY = 1;
    private double rotationDeg;
    private double speedPerSecondX;
    private double speedPerSecondY;
    private double accelerationPerSecondX;
    private double accelerationPerSecondY;
    private double rotationDegSpeedPerSecond;
    protected List<Shape> children;

    public Shape(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public abstract Type getType();

    public void updatePosition(double secSinceLastFrame) {
        speedPerSecondX += accelerationPerSecondX * secSinceLastFrame;
        speedPerSecondY += accelerationPerSecondY * secSinceLastFrame;

        posX += speedPerSecondX * secSinceLastFrame;
        posY += speedPerSecondY * secSinceLastFrame;

        rotationDeg += rotationDegSpeedPerSecond * secSinceLastFrame;
    }

    public abstract void update(double secSinceLastFrame);

    public abstract void update(double secSinceLastFrame, Shape parent);

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setScaleX(double scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleY(double scaleY) {
        this.scaleY = scaleY;
    }

    public void setRotationDeg(double rotationDeg) {
        this.rotationDeg = rotationDeg;
    }

    public void setSpeedPerSecondX(double speedPerSecondX) {
        this.speedPerSecondX = speedPerSecondX;
    }

    public void setSpeedPerSecondY(double speedPerSecondY) {
        this.speedPerSecondY = speedPerSecondY;
    }

    public void setAccelerationPerSecondX(double accelerationPerSecondX) {
        this.accelerationPerSecondX = accelerationPerSecondX;
    }

    public void setAccelerationPerSecondY(double accelerationPerSecondY) {
        this.accelerationPerSecondY = accelerationPerSecondY;
    }

    public void setRotationDegSpeedPerSecond(double rotationDegSpeedPerSecond) {
        this.rotationDegSpeedPerSecond = rotationDegSpeedPerSecond;
    }

    public int getId() {
        return id;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public double getRotationDeg() {
        return rotationDeg;
    }

    public double getSpeedPerSecondX() {
        return speedPerSecondX;
    }

    public double getSpeedPerSecondY() {
        return speedPerSecondY;
    }

    public double getAccelerationPerSecondX() {
        return accelerationPerSecondX;
    }

    public double getAccelerationPerSecondY() {
        return accelerationPerSecondY;
    }

    public double getRotationDegSpeedPerSecond() {
        return rotationDegSpeedPerSecond;
    }

    public void addChild(Shape child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return Double.compare(shape.posX, posX) == 0 &&
                Double.compare(shape.posY, posY) == 0 &&
                Double.compare(shape.scaleX, scaleX) == 0 &&
                Double.compare(shape.scaleY, scaleY) == 0 &&
                Double.compare(shape.rotationDeg, rotationDeg) == 0 &&
                Double.compare(shape.speedPerSecondX, speedPerSecondX) == 0 &&
                Double.compare(shape.speedPerSecondY, speedPerSecondY) == 0 &&
                Double.compare(shape.accelerationPerSecondX, accelerationPerSecondX) == 0 &&
                Double.compare(shape.accelerationPerSecondY, accelerationPerSecondY) == 0 &&
                Double.compare(shape.rotationDegSpeedPerSecond, rotationDegSpeedPerSecond) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(posX, posY, scaleX, scaleY, rotationDeg, speedPerSecondX, speedPerSecondY, accelerationPerSecondX, accelerationPerSecondY, rotationDegSpeedPerSecond);
    }

    public enum Type {
        ELLIPSE, COMPOSITE, TRIANGLE, RECTANGLE, EMPTY
    }
}
