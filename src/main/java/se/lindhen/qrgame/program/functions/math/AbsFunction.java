package se.lindhen.qrgame.program.functions.math;

public class AbsFunction extends UnaryMathFunction {
    public AbsFunction() {
        super("abs");
    }

    @Override
    public double calculate(double arg) {
        return Math.abs(arg);
    }
}
