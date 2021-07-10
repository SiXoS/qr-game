package se.lindhen.qrgame;

import se.lindhen.qrgame.parser.ParseException;
import se.lindhen.qrgame.parser.ProgramParser;
import se.lindhen.qrgame.program.Program;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Util {

    public static Program readProgramFromStream(InputStream resourceAsStream) throws IOException {
        try {
            return ProgramParser.parseProgram(resourceAsStream, StandardCharsets.UTF_8);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
