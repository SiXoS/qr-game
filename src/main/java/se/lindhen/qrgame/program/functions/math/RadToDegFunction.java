package se.lindhen.qrgame.program.functions.math;

public class RadToDegFunction extends UnaryMathFunction {
    public RadToDegFunction() {
        super("radToDeg");
    }

    @Override
    public double calculate(double arg) {
        return Math.toRadians(arg);
    }
}
