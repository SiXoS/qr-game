package se.lindhen.qrgame.program;

import se.lindhen.qrgame.program.statements.Statement;

public class InputCode {

    private final int buttonIdVar;
    private final int pressedElseReleasedVarId;
    private final Statement code;

    public InputCode(int buttonIdVar, int pressedElseReleasedVarId, Statement code) {
        this.buttonIdVar = buttonIdVar;
        this.pressedElseReleasedVarId = pressedElseReleasedVarId;
        this.code = code;
    }

    public void run(Program program) {
        for (InputManager.ButtonEvent buttonEvent : program.getInputManager().getButtonEvents()) {
            program.pushToStack((double) buttonEvent.button.getId());
            program.pushToStack(buttonEvent.pressedElseReleased);
            code.run(program);
            program.popFromStack();
            program.popFromStack();
        }
        program.getInputManager().clear();
    }

    public int getButtonIdVar() {
        return buttonIdVar;
    }

    public int getPressedElseReleasedVarId() {
        return pressedElseReleasedVarId;
    }

    public Statement getCode() {
        return code;
    }
}
