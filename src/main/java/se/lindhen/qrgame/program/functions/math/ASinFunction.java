package se.lindhen.qrgame.program.functions.math;

public class ASinFunction extends UnaryMathFunction {
    public ASinFunction() {
        super("aSin");
    }

    @Override
    public double calculate(double arg) {
        return Math.asin(arg);
    }
}
