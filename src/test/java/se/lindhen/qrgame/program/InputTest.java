package se.lindhen.qrgame.program;

import org.junit.Test;
import se.lindhen.qrgame.Util;
import se.lindhen.qrgame.bytecode.QgCompiler;
import se.lindhen.qrgame.bytecode.QgDecompiler;
import se.lindhen.qrgame.program.expressions.AssignExpression;
import se.lindhen.qrgame.program.statements.BlockStatement;
import se.lindhen.qrgame.program.statements.ExpressionStatement;

import java.io.IOException;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

public class InputTest {

    @Test
    public void testPressButton() throws IOException {
        Program program = Util.readProgramFromStream(getClass().getResourceAsStream("/tests/input.qg"));
        int outVarId = getOutVarId(program);
        Consumer<Integer> iteration = program.initializeAndPrepareRun();
        InputManager inputManager = program.getInputManager();
        inputManager.triggerButton(InputManager.Input.LEFT_LEFT, true);
        iteration.accept(100);
        assertEquals(5.0, (double) program.getVariable(outVarId), 0.01);
        inputManager.triggerButton(InputManager.Input.RIGHT_LEFT, true);
        iteration.accept(100);
        assertEquals(41.0, (double) program.getVariable(outVarId), 0.01);
    }

    @Test
    public void testReleaseButton() throws IOException {
        Program program = Util.readProgramFromStream(getClass().getResourceAsStream("/tests/input.qg"));
        int outVarId = getOutVarId(program);
        Consumer<Integer> iteration = program.initializeAndPrepareRun();
        InputManager inputManager = program.getInputManager();
        inputManager.triggerButton(InputManager.Input.LEFT_LEFT, false);
        iteration.accept(100);
        assertEquals(14.0, (double) program.getVariable(outVarId), 0.01);
        inputManager.triggerButton(InputManager.Input.RIGHT_LEFT, false);
        iteration.accept(100);
        assertEquals(122.0, (double) program.getVariable(outVarId), 0.01);
    }

    @Test
    public void pressingAndReleasingButtonShouldTriggerBothEvents() throws IOException {
        Program program = Util.readProgramFromStream(getClass().getResourceAsStream("/tests/input.qg"));
        int outVarId = getOutVarId(program);
        Consumer<Integer> iteration = program.initializeAndPrepareRun();
        InputManager inputManager = program.getInputManager();
        inputManager.triggerButton(InputManager.Input.LEFT_LEFT, true);
        inputManager.triggerButton(InputManager.Input.LEFT_LEFT, false);
        iteration.accept(100);
        assertEquals(30.0, (double) program.getVariable(outVarId), 0.01);
    }

    @Test
    public void sameButtonTwiceShouldTriggerBothEvents() throws IOException {
        Program program = Util.readProgramFromStream(getClass().getResourceAsStream("/tests/input.qg"));
        int outVarId = getOutVarId(program);
        Consumer<Integer> iteration = program.initializeAndPrepareRun();
        InputManager inputManager = program.getInputManager();
        inputManager.triggerButton(InputManager.Input.LEFT_LEFT, true);
        inputManager.triggerButton(InputManager.Input.LEFT_LEFT, true);
        iteration.accept(100);
        assertEquals(21.0, (double) program.getVariable(outVarId), 0.01);
    }

    @Test
    public void orderOfButtonsMatter() throws IOException {
        Program program = Util.readProgramFromStream(getClass().getResourceAsStream("/tests/input.qg"));
        int outVarId = getOutVarId(program);
        Consumer<Integer> iteration = program.initializeAndPrepareRun();
        InputManager inputManager = program.getInputManager();
        inputManager.triggerButton(InputManager.Input.LEFT_LEFT, true);
        inputManager.triggerButton(InputManager.Input.RIGHT_BOTTOM, true);
        inputManager.triggerButton(InputManager.Input.LEFT_BOTTOM, true);
        inputManager.triggerButton(InputManager.Input.RIGHT_TOP, false);
        inputManager.triggerButton(InputManager.Input.RIGHT_RIGHT, false);
        inputManager.triggerButton(InputManager.Input.LEFT_TOP, true);
        inputManager.triggerButton(InputManager.Input.LEFT_RIGHT, false);
        iteration.accept(100);
        assertEquals(6692.0, (double) program.getVariable(outVarId), 0.01);
    }

    @Test
    public void testInputWhen() throws IOException {
        Program program = Util.readProgramFromStream(getClass().getResourceAsStream("/tests/inputWhen.qg"));
        Program decompiled = new QgDecompiler(new QgCompiler(program).compile()).decompile();
        int outVarId = getOutVarId(program);
        Consumer<Integer> iteration = decompiled.initializeAndPrepareRun();
        InputManager inputManager = decompiled.getInputManager();
        inputManager.triggerButton(InputManager.Input.LEFT_TOP, true);
        iteration.accept(100);
        assertEquals(10, (double) decompiled.getVariable(outVarId), 0.01);
        inputManager.triggerButton(InputManager.Input.LEFT_BOTTOM, true);
        iteration.accept(100);
        assertEquals(12, (double) decompiled.getVariable(outVarId), 0.01);
    }

    private int getOutVarId(Program program) {
        return ((AssignExpression)((ExpressionStatement)((BlockStatement) program.getInitialisation()).getStatements().get(0)).getExpression()).getAssignTo();
    }

}
