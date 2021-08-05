package se.lindhen.qrgame.program;

import org.junit.Test;
import se.lindhen.qrgame.Util;
import se.lindhen.qrgame.program.drawings.Shape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

public class DrawTest {

    @Test
    public void testDraw() throws IOException {
        Program program = Util.readProgramFromStream(getClass().getResourceAsStream("/tests/draw.qg"));
        Consumer<Integer> iteration = program.initializeAndPrepareRun();
        assertEquals(0, program.getDrawings().size());
        iteration.accept(100);
        List<Shape> drawings = new ArrayList<>(program.getDrawings());
        assertEquals(2, drawings.size());
        assertEquals(Shape.Type.TRIANGLE, drawings.get(0).getType());
        assertEquals(Shape.Type.ELLIPSE, drawings.get(1).getType());
    }

}
