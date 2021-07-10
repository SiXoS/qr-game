package se.lindhen.qrgame.bytecode;

public enum CommandCode {
    ASSIGN(0, true),
    GET_VARIABLE(1, true),
    LITERAL_NUMBER(2, true),
    METHOD_CALL(3, true),
    LITERAL_NULL(0, false),
    LITERAL_BOOL(1, false),
    IF(2, false),
    ADD(3, false),
    FUNCTION(4, false),
    WHILE(5, false),
    GET_AND_MODIFY(6, false),
    STRUCT_ASSIGN(7, false),
    MULTIPLY(8, false),
    DIVIDE(9, false),
    LESS(10, false),
    GREATER(11, false),
    AND(12, false),
    OR(13, false),
    NOT(14, false),
    CONDITIONAL(15, false),
    WHEN(16, false),
    EQUALS(17, false),
    TYPE_FETCH(18, false),
    BLOCK(19, false),
    NEGATE(20, false),
    EXPRESSION_STATEMENT(21, false),
    NEW_STRUCT(22, false),
    STRUCT_FETCH(23, false),
    MODULO(24, false),
    FOREACH(25, false),
    RETURN(26, false),
    STACK_VAR(27, false),
    INTERRUPT(28, false);

    private final int code;
    private final boolean common;

    CommandCode(int code, boolean common) {
        if (common && code >= 4) {
            throw new IllegalArgumentException("There are only 4 common commands");
        }
        this.code = code;
        this.common = common;
    }

    public static CommandCode fromParams(boolean isCommon, int code) {
        for (CommandCode commandCode : CommandCode.values()) {
            if (commandCode.common == isCommon && commandCode.code == code) {
                return commandCode;
            }
        }
        throw new IllegalArgumentException("No command with params common: " + isCommon + ", code: " + code);
    }

    public int getCode() {
        return code;
    }

    public boolean isCommon() {
        return common;
    }
}
