package se.lindhen.qrgame.program.functions.math;

public class SqrtFunction extends UnaryMathFunction {
    public SqrtFunction() {
        super("sqrt");
    }

    @Override
    public double calculate(double arg) {
        return Math.sqrt(arg);
    }
}
