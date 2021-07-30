package se.lindhen.qrgame.program.drawings;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShapeTest {

    private final ShapeFactory shapeFactory = new DefaultShapeFactory();

    @Test
    public void testVelocity() {
        Shape rect = shapeFactory.createRect(10, 20, 30, 40);
        rect.setSpeedPerSecondX(2);
        rect.setSpeedPerSecondY(1);
        rect.update(5);
        assertEquals(20, rect.getPosX(), 0.01);
        assertEquals(25, rect.getPosY(), 0.01);
        rect.update(0.1);
        assertEquals(20.2, rect.getPosX(), 0.01);
        assertEquals(25.1, rect.getPosY(), 0.01);
    }

    @Test
    public void testAcceleration() {
        Shape rect = shapeFactory.createRect(10, 20, 30, 40);
        rect.setSpeedPerSecondX(2);
        rect.setSpeedPerSecondY(1);
        rect.setAccelerationPerSecondX(3);
        rect.setAccelerationPerSecondY(4);
        rect.update(5);
        assertEquals(95, rect.getPosX(), 0.01);
        assertEquals(125, rect.getPosY(), 0.01);
        rect.update(0.1);
        assertEquals(96.73, rect.getPosX(), 0.01);
        assertEquals(127.14, rect.getPosY(), 0.01);
    }

    @Test
    public void testRotationSpeed() {
        Shape triangle = shapeFactory.createTriangle(10, 20, 30, 40);
        triangle.setRotationDeg(45);
        triangle.setRotationDegSpeedPerSecond(10);
        triangle.update(2);
        assertEquals(65, triangle.getRotationDeg(), 0.01);
        triangle.update(0.5);
        assertEquals(70, triangle.getRotationDeg(), 0.01);
    }

}
