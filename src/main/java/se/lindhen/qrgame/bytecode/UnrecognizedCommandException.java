package se.lindhen.qrgame.bytecode;

public class UnrecognizedCommandException extends RuntimeException {

    private final boolean isFallback;

    public UnrecognizedCommandException(String message, boolean isFallback) {
        super(message);
        this.isFallback = isFallback;
    }

    public UnrecognizedCommandException(String message, boolean isFallback, Throwable cause) {
        super(message, cause);
        this.isFallback = isFallback;
    }

    public boolean isFallback() {
        return isFallback;
    }
}
