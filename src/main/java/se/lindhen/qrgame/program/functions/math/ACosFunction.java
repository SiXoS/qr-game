package se.lindhen.qrgame.program.functions.math;

public class ACosFunction extends UnaryMathFunction {
    public ACosFunction() {
        super("aCos");
    }

    @Override
    public double calculate(double arg) {
        return Math.acos(arg);
    }
}
