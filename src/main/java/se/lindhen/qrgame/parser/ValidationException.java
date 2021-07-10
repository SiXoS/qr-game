package se.lindhen.qrgame.parser;

import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {

    private final ValidationResult validationResult;

    public ValidationException(ValidationResult result) {
        super(result.getMessages().stream().collect(Collectors.joining(System.lineSeparator())));
        this.validationResult = result;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }
}
