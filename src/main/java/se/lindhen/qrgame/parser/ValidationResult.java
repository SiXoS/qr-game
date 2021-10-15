package se.lindhen.qrgame.parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ValidationResult {

    private final boolean isValid;
    private final List<String> messages;

    public ValidationResult(boolean isValid, List<String> messages) {
        this.isValid = isValid;
        this.messages = messages;
    }

    public static ValidationResult valid() {
        return new ValidationResult(true, Collections.emptyList());
    }

    public static ValidationResult invalid(ParserRuleContext ctx, String message) {
        String error = message;
        if (ctx != null) {
            String relevantCode = ctx.start.getInputStream().getText(new Interval(ctx.start.getStartIndex(), ctx.stop.getStopIndex()));
            error = "Error at " + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine() + "; \"" + message + "\" in: \"" + relevantCode + "\"";
        }
        return new ValidationResult(false, Collections.singletonList(error));
    }

    public static ValidationResult multiple(Collection<ValidationResult> validationResults) {
        ArrayList<String> messages = new ArrayList<>();
        boolean isValid = true;
        for (ValidationResult result : validationResults) {
            isValid &= result.isValid();
            messages.addAll(result.getMessages());
        }
        return new ValidationResult(isValid, messages);
    }

    public boolean isValid() {
        return isValid;
    }

    public List<String> getMessages() {
        return messages;
    }
}
