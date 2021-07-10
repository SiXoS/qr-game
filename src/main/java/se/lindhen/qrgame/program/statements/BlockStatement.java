package se.lindhen.qrgame.program.statements;

import se.lindhen.qrgame.program.Program;

import java.util.ArrayList;

public class BlockStatement extends Statement {

    private final ArrayList<Statement> statements;

    public BlockStatement(ArrayList<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public void runInternal(Program program) {
        for (Statement statement : statements) {
            statement.run(program);
        }
    }

    @Override
    public boolean alwaysReturns() {
        return statements.stream().anyMatch(Statement::alwaysReturns);
    }

    public ArrayList<Statement> getStatements() {
        return statements;
    }

}
