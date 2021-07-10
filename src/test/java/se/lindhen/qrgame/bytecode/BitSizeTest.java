package se.lindhen.qrgame.bytecode;

import org.junit.Ignore;
import org.junit.Test;
import se.lindhen.qrgame.Util;
import se.lindhen.qrgame.program.Program;

import java.io.IOException;
import java.util.Map;

public class BitSizeTest {

    @Test
    @Ignore
    public void compileSnake() throws IOException {
        Program program = Util.readProgramFromStream(getClass().getResourceAsStream("/snake.qg"));
        QgCompiler qgCompiler = new QgCompiler(program);
        qgCompiler.compile();
        qgCompiler.getContextualBitsWritten()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
        System.out.println("total: " + qgCompiler.getBytesWritten());
    }

}
