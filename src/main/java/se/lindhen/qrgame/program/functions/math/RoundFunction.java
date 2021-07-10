package se.lindhen.qrgame.program.functions.math;

public class RoundFunction extends UnaryMathFunction {
    public RoundFunction() {
        super("round");
    }

    @Override
    public double calculate(double arg) {
        return Math.round(arg);
    }
}
