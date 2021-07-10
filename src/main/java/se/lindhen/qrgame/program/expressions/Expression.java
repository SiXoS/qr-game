package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.types.Type;

import java.util.Arrays;
import java.util.Collection;

public abstract class Expression {

    private final Type type;
    private final Collection<Expression> subExpressions;

    public Expression(Type type, Collection<Expression> subExpressions) {
        this.type = type;
        this.subExpressions = subExpressions;
    }

    public Expression(Type type, Expression... expressions) {
        this(type, Arrays.asList(expressions));
    }

    public Type getType() {
        return type;
    }

    public abstract Object calculate(Program program);

    public Collection<Expression> getSubExpressions() {
        return subExpressions;
    }

}
