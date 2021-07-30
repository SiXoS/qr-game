package se.lindhen.qrgame.program;

import org.junit.Test;
import se.lindhen.qrgame.Util;
import se.lindhen.qrgame.program.expressions.AssignExpression;
import se.lindhen.qrgame.program.statements.BlockStatement;
import se.lindhen.qrgame.program.statements.ExpressionStatement;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class InputTest {

    @Test
    public void testPressButton() throws IOException {
        Program program = Util.readProgramFromStream(getClass().getResourceAsStream("/tests/input.qg"));
        int outVarId = getOutVarId(program);
        Runnable iteration = program.initializeAndPrepareRun();
        InputManager inputManager = program.getInputManager();
        inputManager.triggerButton(InputManager.Input.LEFT_LEFT, true);
        iteration.run();
        assertEquals(5.0, (double) program.getVariable(outVarId), 0.01);
        inputManager.triggerButton(InputManager.Input.RIGHT_LEFT, true);
        iteration.run();
        assertEquals(41.0, (double) program.getVariable(outVarId), 0.01);
    }

    @Test
    public void testReleaseButton() throws IOException {
        Program program = Util.readProgramFromStream(getClass().getResourceAsStream("/tests/input.qg"));
        int outVarId = getOutVarId(program);
        Runnable iteration = program.initializeAndPrepareRun();
        InputManager inputManager = program.getInputManager();
        inputManager.triggerButton(InputManager.Input.LEFT_LEFT, false);
        iteration.run();
        assertEquals(14.0, (double) program.getVariable(outVarId), 0.01);
        inputManager.triggerButton(InputManager.Input.RIGHT_LEFT, false);
        iteration.run();
        assertEquals(122.0, (double) program.getVariable(outVarId), 0.01);
    }

    @Test
    public void pressingAndReleasingButtonShouldTriggerBothEvents() throws IOException {
        Program program = Util.readProgramFromStream(getClass().getResourceAsStream("/tests/input.qg"));
        int outVarId = getOutVarId(program);
        Runnable iteration = program.initializeAndPrepareRun();
        InputManager inputManager = program.getInputManager();
        inputManager.triggerButton(InputManager.Input.LEFT_LEFT, true);
        inputManager.triggerButton(InputManager.Input.LEFT_LEFT, false);
        iteration.run();
        assertEquals(30.0, (double) program.getVariable(outVarId), 0.01);
    }

    @Test
    public void sameButtonTwiceShouldTriggerBothEvents() throws IOException {
        Program program = Util.readProgramFromStream(getClass().getResourceAsStream("/tests/input.qg"));
        int outVarId = getOutVarId(program);
        Runnable iteration = program.initializeAndPrepareRun();
        InputManager inputManager = program.getInputManager();
        inputManager.triggerButton(InputManager.Input.LEFT_LEFT, true);
        inputManager.triggerButton(InputManager.Input.LEFT_LEFT, true);
        iteration.run();
        assertEquals(21.0, (double) program.getVariable(outVarId), 0.01);
    }

    @Test
    public void orderOfButtonsMatter() throws IOException {
        Program program = Util.readProgramFromStream(getClass().getResourceAsStream("/tests/input.qg"));
        int outVarId = getOutVarId(program);
        Runnable iteration = program.initializeAndPrepareRun();
        InputManager inputManager = program.getInputManager();
        inputManager.triggerButton(InputManager.Input.LEFT_LEFT, true);
        inputManager.triggerButton(InputManager.Input.RIGHT_BOTTOM, true);
        inputManager.triggerButton(InputManager.Input.LEFT_BOTTOM, true);
        inputManager.triggerButton(InputManager.Input.RIGHT_TOP, false);
        inputManager.triggerButton(InputManager.Input.RIGHT_RIGHT, false);
        inputManager.triggerButton(InputManager.Input.LEFT_TOP, true);
        inputManager.triggerButton(InputManager.Input.LEFT_RIGHT, false);
        iteration.run();
        assertEquals(6692.0, (double) program.getVariable(outVarId), 0.01);
    }

    private int getOutVarId(Program program) {
        return ((AssignExpression)((ExpressionStatement)((BlockStatement) program.getInitialisation()).getStatements().get(0)).getExpression()).getAssignTo();
    }

}
