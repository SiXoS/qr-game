package se.lindhen.qrgame.program.functions.math;

public class IntModFunction extends BinaryMathFunction {
    public IntModFunction() {
        super("intMod");
    }

    @Override
    public double calculate(double arg1, double arg2) {
        return (long) arg1 % (long) arg2;
    }
}
