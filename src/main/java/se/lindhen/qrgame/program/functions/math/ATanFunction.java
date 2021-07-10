package se.lindhen.qrgame.program.functions.math;

public class ATanFunction extends UnaryMathFunction {
    public ATanFunction() {
        super("atan");
    }

    @Override
    public double calculate(double arg) {
        return Math.atan(arg);
    }
}
