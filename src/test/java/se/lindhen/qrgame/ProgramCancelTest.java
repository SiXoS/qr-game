package se.lindhen.qrgame;

import org.junit.Assert;
import org.junit.Test;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.AssignExpression;
import se.lindhen.qrgame.program.statements.BlockStatement;
import se.lindhen.qrgame.program.statements.ExpressionStatement;

import java.io.IOException;
import java.util.function.Consumer;

public class ProgramCancelTest {

    @Test
    public void testCancelWhile() throws IOException, InterruptedException {
        Object outValue = testInfiniteProgramCanBeCanceled("/tests/cancel/cancelWhile.qg");
        Assert.assertEquals(0, (double) outValue, 0.01);
    }

    @Test
    public void testCancelFunction() throws IOException, InterruptedException {
        Object outValue = testInfiniteProgramCanBeCanceled("/tests/cancel/cancelFunction.qg");
        Assert.assertNull(outValue);
    }

    private Object testInfiniteProgramCanBeCanceled(String testFile) throws IOException, InterruptedException {
        Program program = Util.readProgramFromStream(getClass().getResourceAsStream(testFile));
        Consumer<Integer> iterationRunner = program.initializeAndPrepareRun();
        Thread programThread = new Thread(() -> iterationRunner.accept(100));
        programThread.start();
        Thread.sleep(1000);
        Assert.assertTrue(programThread.isAlive());
        program.cancel();
        Thread.sleep(10);
        Assert.assertFalse(programThread.isAlive());
        int outVarId = getOutVarId(program);
        return program.getVariable(outVarId);
    }

    private int getOutVarId(Program program) {
        return ((AssignExpression)((ExpressionStatement)((BlockStatement) program.getInitialisation()).getStatements().get(0)).getExpression()).getAssignTo();
    }

}
