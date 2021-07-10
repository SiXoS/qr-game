package se.lindhen.qrgame.program.functions.math;

public class IntDivFunction extends BinaryMathFunction {
    public IntDivFunction() {
        super("intDiv");
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    public double calculate(double arg1, double arg2) {
        return (long) arg1 / (long) arg2;
    }
}
