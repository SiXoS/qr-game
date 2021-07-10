package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.StructValue;
import se.lindhen.qrgame.program.types.StructType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class StructInstantiationExpression extends Expression {

    private final int structId;

    public StructInstantiationExpression(int structId, Expression... arguments) {
        super(new StructType(structId), arguments);
        this.structId = structId;
    }

    public int getStructId() {
        return structId;
    }

    @Override
    public Object calculate(Program program) {
        Collection<Expression> fields = getSubExpressions();
        ArrayList<Object> values = calculateExpressions(program, fields);
        return new StructValue(values);
    }

    private ArrayList<Object> calculateExpressions(Program program, Collection<Expression> fields) {
        return fields
                .stream()
                .map(e -> e.calculate(program))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
