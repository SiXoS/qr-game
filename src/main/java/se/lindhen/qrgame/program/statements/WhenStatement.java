package se.lindhen.qrgame.program.statements;

import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.Expression;

import java.util.Map;

public class WhenStatement extends Statement {

    private final Expression toCompare;
    private final Map<Object, Statement> cases;
    private final Statement defaultCase;

    public WhenStatement(Expression toCompare, Map<Object, Statement> cases, Statement defaultCase) {
        this.toCompare = toCompare;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }

    @Override
    public void runInternal(Program program) {
        Statement toRun = cases.getOrDefault(toCompare.calculate(program), defaultCase);
        if (toRun != null) {
            toRun.run(program);
        }
    }

    @Override
    public boolean alwaysReturns() {
        return defaultCase == null && cases.values().stream().allMatch(Statement::alwaysReturns);
    }

    public Expression getToCompare() {
        return toCompare;
    }

    public Map<Object, Statement> getCases() {
        return cases;
    }

    public Statement getDefaultCase() {
        return defaultCase;
    }
}
