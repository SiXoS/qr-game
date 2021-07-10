package se.lindhen.qrgame.program.functions.math;

public class LogFunction extends BinaryMathFunction {
    public LogFunction() {
        super("log");
    }

    @Override
    public double calculate(double arg1, double arg2) {
        return Math.log(arg2)/Math.log(arg1);
    }
}
