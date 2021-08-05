package se.lindhen.qrgame.cli;

public enum CliErrorCode {
    UNKNOWN(1),
    TARGET_EXISTS(2),
    TARGET_FILE_PERMISSION_DENIED(3),
    SOURCE_FILE_PERMISSION_DENIED(4),
    SOURCE_NOT_FOUND(5),
    ILLEGAL_CHARSET(6),
    PARSE_FAILED(7),
    VALIDATE_FAILED(8),
    BYTECODE_TOO_LARGE(9),
    TARGET_DIR_NOT_FOUND(10);

    private final int exitCode;

    CliErrorCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return exitCode;
    }
}
