package se.lindhen.qrgame;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import se.lindhen.qrgame.bytecode.QgCompiler;
import se.lindhen.qrgame.bytecode.QgDecompiler;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.AssignExpression;
import se.lindhen.qrgame.program.statements.BlockStatement;
import se.lindhen.qrgame.program.statements.ExpressionStatement;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static se.lindhen.qrgame.Util.readProgramFromStream;

@RunWith(Parameterized.class)
public class QgFileTest {

    @Parameterized.Parameters(name = "{0}")
    public static Object[][] getTestCases() throws URISyntaxException, FileNotFoundException {
        return Util.readTestCasesFromFiles(Arrays.asList("/tests/happy"), "out", TestCase::new);
    }

    @Parameterized.Parameter()
    public TestCase testCase;

    @Test
    public void parseAndRun() throws IOException {
        Program program = readProgramFromStream(new ByteArrayInputStream(testCase.program.getBytes()));
        Consumer<Integer> iteration = program.initializeAndPrepareRun();
        iteration.accept(100);
        Assert.assertEquals(testCase.result, (double) program.getVariable(getOutVarId(program)), 0.001);
    }

    @Test
    public void testCompileDecompileAndRun() throws IOException {
        Program program = readProgramFromStream(new ByteArrayInputStream(testCase.program.getBytes()));
        QgCompiler compiler = new QgCompiler(program);
        byte[] compiled = compiler.compile();
        QgDecompiler qgDecompiler = new QgDecompiler(compiled);
        Program decompiledProgram = qgDecompiler.decompile();
        Consumer<Integer> iteration = decompiledProgram.initializeAndPrepareRun();
        iteration.accept(100);
        Assert.assertEquals(testCase.result, (double) decompiledProgram.getVariable(getOutVarId(program)), 0.001);
    }

    private int getOutVarId(Program program) {
        return ((AssignExpression)((ExpressionStatement)((BlockStatement) program.getInitialisation()).getStatements().get(0)).getExpression()).getAssignTo();
    }

    private static class TestCase extends Util.FileTestCase {
        public double result;

        public TestCase(String name) {
            super(name);
        }

        @Override
        public void setResult(String fileContent) {
            result = Double.parseDouble(fileContent);
        }
    }

}
