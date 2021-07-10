package se.lindhen.qrgame.program.functions.math;

public class TanFunction extends UnaryMathFunction {
    public TanFunction() {
        super("tan");
    }

    @Override
    public double calculate(double arg) {
        return Math.tan(arg);
    }
}
