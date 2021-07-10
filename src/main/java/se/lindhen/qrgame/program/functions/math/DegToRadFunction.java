package se.lindhen.qrgame.program.functions.math;

public class DegToRadFunction extends UnaryMathFunction {
    public DegToRadFunction() {
        super("degToRad");
    }

    @Override
    public double calculate(double arg) {
        return Math.toRadians(arg);
    }
}
