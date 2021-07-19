package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.NumberType;
import se.lindhen.qrgame.program.types.Type;

public class NumberExpression extends Expression {

    private final double number;

    public NumberExpression(double number) {
        super(NumberType.NUMBER_TYPE);
        this.number = number;
    }

    @Override
    public Object calculate(Program program) {
        return number;
    }

    public double getNumber() {
        return number;
    }
}
