package se.lindhen.qrgame.program.functions.math;

public class PowFunction extends BinaryMathFunction {
    public PowFunction() {
        super("pow");
    }

    @Override
    public double calculate(double arg1, double arg2) {
        return Math.pow(arg1, arg2);
    }
}
