package se.lindhen.qrgame.bytecode;

public class UnrecognizedCommandException extends RuntimeException {

    private boolean isFallback;

    public UnrecognizedCommandException() {
    }

    public UnrecognizedCommandException(String message, boolean isFallback) {
        super(message);
        this.isFallback = isFallback;
    }

    public UnrecognizedCommandException(String message, boolean isFallback, Throwable cause) {
        super(message, cause);
        this.isFallback = isFallback;
    }

    public UnrecognizedCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public boolean isFallback() {
        return isFallback;
    }
}
