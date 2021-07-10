package se.lindhen.qrgame.program.functions.math;

public class EFunction extends NullaryMathFunction {
    public EFunction() {
        super("e");
    }

    @Override
    public double calculate() {
        return Math.E;
    }
}
