package se.lindhen.qrgame.program.statements;

import se.lindhen.qrgame.program.expressions.Expression;
import se.lindhen.qrgame.program.expressions.WhenExpression;
import se.lindhen.qrgame.program.types.Type;

import java.util.HashMap;
import java.util.Map;

public class WhenBuilder {

    private Expression toCompare;
    private final boolean isStatement;
    private final Map<Object, Statement> statementCases = new HashMap<>();
    private final Map<Object, Expression> expressionCases = new HashMap<>();
    private Statement defaultStatementCase;
    private Expression defaultExpressionCase;

    private WhenBuilder(boolean isStatement) {
        this.isStatement = isStatement;
    }

    public static WhenBuilder whenStatementBuilder() {
        return new WhenBuilder(true);
    }

    public static WhenBuilder whenExpressionBuilder() {
        return new WhenBuilder(false);
    }

    public void setToCompare(Expression toCompare) {
        this.toCompare = toCompare;
    }

    public void setDefaultCase(Statement defaultCase) {
        assert isStatement;
        this.defaultStatementCase = defaultCase;
    }

    public void setDefaultCase(Expression defaultCase) {
        assert !isStatement;
        this.defaultExpressionCase = defaultCase;
    }

    public void putClause(Object theCase, Statement statement) {
        assert isStatement;
        statementCases.put(theCase, statement);
    }

    public void putClause(Object theCase, Expression expression) {
        assert !isStatement;
        expressionCases.put(theCase, expression);
    }

    public Type getParameterType() {
        return toCompare.getType();
    }

    public WhenStatement buildStatement() {
        assert isStatement;
        return new WhenStatement(toCompare, statementCases, defaultStatementCase);
    }

    public WhenExpression buildExpression() {
        assert !isStatement;
        return new WhenExpression(toCompare, expressionCases, defaultExpressionCase);
    }
}
