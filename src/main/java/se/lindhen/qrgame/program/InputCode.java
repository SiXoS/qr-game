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
            storeInputParameter(program, buttonIdVar, (double) buttonEvent.button.getId());
            storeInputParameter(program, pressedElseReleasedVarId, buttonEvent.pressedElseReleased);
            code.run(program);
        }
        program.getInputManager().clear();
    }

    protected void storeInputParameter(Program program, int buttonIdVar, Object value) {
        program.setVariable(buttonIdVar, value);
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
