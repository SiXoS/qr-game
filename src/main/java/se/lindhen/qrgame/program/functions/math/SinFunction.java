package se.lindhen.qrgame.program.functions.math;

public class SinFunction extends UnaryMathFunction {
    public SinFunction() {
        super("sin");
    }

    @Override
    public double calculate(double arg) {
        return Math.sin(arg);
    }
}
