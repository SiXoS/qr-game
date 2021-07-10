package se.lindhen.qrgame.program.functions.math;

public class FloorFunction extends UnaryMathFunction {
    public FloorFunction() {
        super("floor");
    }

    @Override
    public double calculate(double arg) {
        return Math.floor(arg);
    }
}
