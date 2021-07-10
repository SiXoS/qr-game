package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.StructValue;

public class StructAssignExpression extends Expression {
    private final Expression structExpression;
    private final int fieldId;
    private final Expression valueExpression;

    public StructAssignExpression(Expression structExpression, int fieldId, Expression valueExpression) {
        super(valueExpression.getType(), structExpression, valueExpression);
        this.structExpression = structExpression;
        this.fieldId = fieldId;
        this.valueExpression = valueExpression;
    }

    @Override
    public Object calculate(Program program) {
        Object value = valueExpression.calculate(program);
        ((StructValue) structExpression.calculate(program)).setField(fieldId, value);
        return value;
    }

    public Expression getStructExpression() {
        return structExpression;
    }

    public int getFieldId() {
        return fieldId;
    }

    public Expression getValueExpression() {
        return valueExpression;
    }
}
