package se.lindhen.qrgame.program.functions.math;

public class ASinFunction extends UnaryMathFunction {
    public ASinFunction() {
        super("asin");
    }

    @Override
    public double calculate(double arg) {
        return Math.asin(arg);
    }
}
