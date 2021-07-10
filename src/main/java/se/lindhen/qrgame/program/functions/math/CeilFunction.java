package se.lindhen.qrgame.program.functions.math;

public class CeilFunction extends UnaryMathFunction {
    public CeilFunction() {
        super("ceil");
    }

    @Override
    public double calculate(double arg) {
        return Math.ceil(arg);
    }
}
