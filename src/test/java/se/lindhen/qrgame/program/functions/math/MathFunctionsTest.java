package se.lindhen.qrgame.program.functions.math;

import org.junit.Assert;
import org.junit.Test;
import se.lindhen.qrgame.program.functions.math.*;

public class MathFunctionsTest {

    @Test
    public void testPowFunction() {
        Assert.assertEquals(8, new PowFunction().calculate(2, 3), 0.01);
    }

    @Test
    public void testIntModFunction() {
        Assert.assertEquals(1, new IntModFunction().calculate(4, 3), 0.01);
    }

    @Test
    public void testExpFunction() {
        Assert.assertEquals(7.38906, new ExpFunction().calculate(2), 0.01);
    }

    @Test
    public void testLogFunction() {
        Assert.assertEquals(2, new LogFunction().calculate(2, 4), 0.01);
    }

    @Test
    public void testFloorFunction() {
        Assert.assertEquals(2, new FloorFunction().calculate(2.9999), 0.01);
    }

    @Test
    public void testTanFunction() {
        Assert.assertEquals(1.5574, new TanFunction().calculate(1), 0.01);
    }

    @Test
    public void testDegToRadFunction() {
        Assert.assertEquals(Math.PI, new DegToRadFunction().calculate(180), 0.01);
    }

    @Test
    public void testASinFunction() {
        Assert.assertEquals(Math.PI/2, new ASinFunction().calculate(1), 0.01);
    }

    @Test
    public void testACosFunction() {
        Assert.assertEquals(0, new ACosFunction().calculate(1), 0.01);
    }

    @Test
    public void testLog10Function() {
        Assert.assertEquals(3, new Log10Function().calculate(1000), 0.01);
    }

    @Test
    public void testRoundFunction() {
        Assert.assertEquals(2, new RoundFunction().calculate(1.8), 0.01);
    }

    @Test
    public void testCosFunction() {
        Assert.assertEquals(0.5403, new CosFunction().calculate(1), 0.01);
    }

    @Test
    public void testLognFunction() {
        Assert.assertEquals(2.3026, new LognFunction().calculate(10), 0.01);
    }

    @Test
    public void testPiFunction() {
        Assert.assertEquals(Math.PI, new PiFunction().calculate(), 0.01);
    }

    @Test
    public void testIntDivFunction() {
        Assert.assertEquals(2, new IntDivFunction().calculate(5, 2), 0.01);
    }

    @Test
    public void testCeilFunction() {
        Assert.assertEquals(2, new CeilFunction().calculate(1.1), 0.01);
    }

    @Test
    public void testAbsFunction() {
        Assert.assertEquals(2, new AbsFunction().calculate(-2), 0.01);
    }

    @Test
    public void testSqrtFunction() {
        Assert.assertEquals(4, new SqrtFunction().calculate(16), 0.01);
    }

    @Test
    public void testSinFunction() {
        Assert.assertEquals(0.8414, new SinFunction().calculate(1), 0.01);
    }

    @Test
    public void testRadToDegFunction() {
        Assert.assertEquals(180, new RadToDegFunction().calculate(Math.PI), 0.01);
    }

    @Test
    public void testATanFunction() {
        Assert.assertEquals(Math.PI/4, new ATanFunction().calculate(1), 0.01);
    }

    @Test
    public void testEFunction() {
        Assert.assertEquals(Math.E, new EFunction().calculate(), 0.01);
    }
}
