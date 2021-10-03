package se.lindhen.qrgame.program;

import se.lindhen.qrgame.parser.ValidationResult;

public class ResultOrInvalidation<T> {

    public final T result;
    public final ValidationResult invalidation;

    private ResultOrInvalidation(T result, ValidationResult invalidation) {
        this.result = result;
        this.invalidation = invalidation;
    }

    public static <T> ResultOrInvalidation<T> valid(T result) {
        return new ResultOrInvalidation<>(result, null);
    }

    public static <T> ResultOrInvalidation<T> invalid(ValidationResult validationResult) {
        assert validationResult != null;
        return new ResultOrInvalidation<>(null, validationResult);
    }

    public boolean hasResult() {
        return invalidation == null;
    }

}
