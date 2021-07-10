package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.Type;
import se.lindhen.qrgame.program.types.TypeType;

public class TypeFetchExpression extends Expression {

    private final Expression valueExpression;

    public TypeFetchExpression(Expression valueExpression) {
        super(new TypeType(valueExpression.getType()), valueExpression);
        this.valueExpression = valueExpression;
    }

    @Override
    public Object calculate(Program program) {
        return valueExpression.getType();
    }
}
