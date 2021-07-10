package se.lindhen.qrgame.program.functions.math;

public class CosFunction extends UnaryMathFunction {
    public CosFunction() {
        super("cos");
    }

    @Override
    public double calculate(double arg) {
        return Math.cos(arg);
    }
}
