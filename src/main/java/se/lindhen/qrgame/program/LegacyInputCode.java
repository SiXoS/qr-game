package se.lindhen.qrgame.program;

import se.lindhen.qrgame.program.statements.Statement;

/**
 * @deprecated Only used for backwards compatibility with V1
 */
@Deprecated
public class LegacyInputCode extends InputCode {

    public LegacyInputCode(int buttonIdVar, int pressedElseReleasedVarId, Statement code) {
        super(buttonIdVar, pressedElseReleasedVarId, code);
    }

    @Override
    protected void storeInputParameter(Program program, int buttonIdVar, Object value) {
        program.setStackVariable(buttonIdVar, value);
    }
}
