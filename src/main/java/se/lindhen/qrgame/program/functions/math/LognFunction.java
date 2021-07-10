package se.lindhen.qrgame.program.functions.math;

public class LognFunction extends UnaryMathFunction {
    public LognFunction() {
        super("logn");
    }

    @Override
    public double calculate(double arg) {
        return Math.log(arg);
    }
}
