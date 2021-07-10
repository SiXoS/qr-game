package se.lindhen.qrgame;

import org.junit.Assert;
import org.junit.Test;
import se.lindhen.qrgame.bytecode.QgCompiler;
import se.lindhen.qrgame.bytecode.QgDecompiler;
import se.lindhen.qrgame.program.Program;

import java.io.IOException;

import static se.lindhen.qrgame.Util.readProgramFromStream;

public class BadCodeTest {

    @Test
    public void testLexerError() throws IOException {
        try {
            readProgramFromStream(getClass().getResourceAsStream("/bad_code/swedish_character_in_code.qc"));
            Assert.fail("Should cause exception");
        } catch (RuntimeException e) {
            Assert.assertTrue(e.getMessage(), e.getMessage().contains("Syntax error"));
            Assert.assertTrue(e.getMessage(), e.getMessage().contains("å"));
            Assert.assertTrue(e.getMessage(), e.getMessage().contains("3:5"));
        }
    }

    @Test
    public void testCompileDecompileSnake() throws IOException {
        Program program = readProgramFromStream(getClass().getResourceAsStream("/snake.qg"));
        new QgDecompiler(new QgCompiler(program).compile()).decompile();
    }

}
