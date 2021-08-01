package se.lindhen.qrgame.cli;

public class CliException extends RuntimeException {

    private final CliErrorCode errorCode;

    public CliException(String message, CliErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CliException(String message, Throwable cause, CliErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CliErrorCode getErrorCode() {
        return errorCode;
    }
}
