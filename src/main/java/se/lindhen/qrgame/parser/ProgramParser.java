package se.lindhen.qrgame.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import se.lindhen.qrgame.QrGameLexer;
import se.lindhen.qrgame.QrGameParser;
import se.lindhen.qrgame.program.Program;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ProgramParser {

    public static Program parseProgram(InputStream code, Charset charset) throws IOException, ValidationException, ParseException {
        ParsingErrorListener errorListener = new ParsingErrorListener();

        QrGameLexer qrGameLexer = new QrGameLexer(new ANTLRInputStream(new InputStreamReader(code, charset)));
        qrGameLexer.removeErrorListeners();
        qrGameLexer.addErrorListener(errorListener);
        CommonTokenStream tokens = new CommonTokenStream(qrGameLexer);

        QrGameParser parser = new QrGameParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        Program program = new Program();
        RuntimeException parseException = null;
        ValidationException validationException = null;
        QrGameTreeListener listener = null;
        try {
            VariableCountListener variableCountListener = new VariableCountListener();
            QrGameParser.ProgramContext programRule = parser.program();
            parseTreeWalker.walk(variableCountListener, programRule);
            listener = new QrGameTreeListener(program, variableCountListener.getOptimizedVariableIds());
            parseTreeWalker.walk(listener, programRule);
        } catch (ValidationException e) {
            validationException = e;
        } catch (RuntimeException e) {
            parseException = e;
        }

        if (!errorListener.getErrors().isEmpty()) {
            throw new ParseException(errorListener.getErrors());
        }
        if (validationException != null) {
            throw validationException;
        }
        if (parseException != null) {
            throw parseException;
        }
        List<ValidationResult> errors = listener.validate()
                .filter(result -> !result.isValid())
                .collect(Collectors.toList());
        if (!errors.isEmpty()) {
            throw new ValidationException(ValidationResult.multiple(errors));
        }
        return program;
    }

}
