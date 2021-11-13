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
    private Color color = Color.FOREGROUND;
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

    public void setColorForeground() {
        color = Color.FOREGROUND;
    }

    public void setColorBackground() {
        color = Color.BACKGROUND;
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

    public Color getColor() {
        return color;
    }

    public void addChild(Shape child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public enum Type {
        ELLIPSE, COMPOSITE, TRIANGLE, RECTANGLE, EMPTY
    }
}
