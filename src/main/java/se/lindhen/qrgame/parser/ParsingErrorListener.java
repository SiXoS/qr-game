package se.lindhen.qrgame.parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Nullable;

import java.util.ArrayList;
import java.util.BitSet;

public class ParsingErrorListener implements ANTLRErrorListener {

    private final ArrayList<String> errors = new ArrayList<>();

    @Override
    public void syntaxError(@NotNull Recognizer<?, ?> recognizer,
                            @Nullable Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            @NotNull String msg,
                            @Nullable RecognitionException e) {
        String tokenString = "";
        if (offendingSymbol != null) {
            String offendingText = offendingSymbol instanceof Token ? ((Token) offendingSymbol).getText() : offendingSymbol.toString();
            tokenString = " at '" + offendingText + "'";
        }
        errors.add("Syntax error" + tokenString + " on line:column " + line + ":" + charPositionInLine + "; " + msg);
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {

    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {

    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {

    }

    public ArrayList<String> getErrors() {
        return errors;
    }
}
