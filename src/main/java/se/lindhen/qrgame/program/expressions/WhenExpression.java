package se.lindhen.qrgame.program.expressions;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.statements.Statement;
import se.lindhen.qrgame.program.types.Type;

import java.util.Map;

public class WhenExpression extends Expression {

    private final Expression toCompare;
    private final Map<Object, Expression> cases;
    private final Expression defaultCase;

    public WhenExpression(Expression toCompare, Map<Object, Expression> cases, Expression defaultCase) {
        super(defaultCase.getType());
        this.toCompare = toCompare;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    @Override
    public Object calculate(Program program) {
        Expression toRun = cases.getOrDefault(toCompare.calculate(program), defaultCase);
        return toRun.calculate(program);
    }

    public Expression getToCompare() {
        return toCompare;
    }

    public Map<Object, Expression> getCases() {
        return cases;
    }

    public Expression getDefaultCase() {
        return defaultCase;
    }
}
