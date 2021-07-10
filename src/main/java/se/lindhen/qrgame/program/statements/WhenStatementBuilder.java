package se.lindhen.qrgame.program.statements;

import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.types.Type;

import java.util.HashMap;
import java.util.Map;

public class WhenStatementBuilder {

    private Expression toCompare;
    private final Map<Object, Statement> cases = new HashMap<>();
    private Statement defaultCase;

    public void setToCompare(Expression toCompare) {
        this.toCompare = toCompare;
    }

    public void setDefaultCase(Statement defaultCase) {
        this.defaultCase = defaultCase;
    }

    public void putClause(Object theCase, Statement statement) {
        cases.put(theCase, statement);
    }

    public Type getParameterType() {
        return toCompare.getType();
    }

    public WhenStatement build() {
        return new WhenStatement(toCompare, cases, defaultCase);
    }
}
