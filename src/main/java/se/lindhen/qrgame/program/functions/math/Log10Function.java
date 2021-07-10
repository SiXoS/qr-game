package se.lindhen.qrgame.program.functions.math;

public class Log10Function extends UnaryMathFunction {
    public Log10Function() {
        super("log10");
    }

    @Override
    public double calculate(double arg) {
        return Math.log10(arg);
    }
}
