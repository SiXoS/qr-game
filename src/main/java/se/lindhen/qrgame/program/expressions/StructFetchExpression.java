package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.StructValue;
import se.lindhen.qrgame.program.types.Type;

public class StructFetchExpression extends Expression {

    private final Expression structExpression;
    private final int fieldId;

    public StructFetchExpression(Expression structExpression, Type returnType, int fieldId) {
        super(returnType, structExpression);
        this.structExpression = structExpression;
        this.fieldId = fieldId;
    }

    public int getFieldId() {
        return fieldId;
    }

    @Override
    public Object calculate(Program program) {
        return ((StructValue)structExpression.calculate(program)).getField(fieldId);
    }
}
