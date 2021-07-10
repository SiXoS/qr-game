package se.lindhen.qrgame.program.functions.math;

public class ExpFunction extends UnaryMathFunction {
    public ExpFunction() {
        super("exp");
    }

    @Override
    public double calculate(double arg) {
        return Math.exp(arg);
    }
}
