package se.lindhen.qrgame.program.functions.math;

public class PiFunction extends NullaryMathFunction {
    public PiFunction() {
        super("pi");
    }

    @Override
    public double calculate() {
        return Math.PI;
    }
}
