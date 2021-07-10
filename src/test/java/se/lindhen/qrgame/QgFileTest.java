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
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static se.lindhen.qrgame.Util.readProgramFromStream;

@RunWith(Parameterized.class)
public class QgFileTest {

    @Parameterized.Parameters(name = "{0}")
    public static Object[][] getTestCases() throws URISyntaxException, FileNotFoundException {
        HashMap<String, TestCase> testCases = new HashMap<>();
        File testFolder = new File(QgFileTest.class.getResource("/tests").toURI());
        for (File file : testFolder.listFiles()) {
            String[] parts = file.getName().split("\\.");
            String testName = parts[0];
            TestCase testCase = testCases.computeIfAbsent(testName, k -> new TestCase(testName));
            if (parts[1].equals("qg")) {
                testCase.program = readFile(file);
            } else if (parts[1].equals("out")) {
                testCase.result = Float.parseFloat(readFile(file));
            } else {
                throw new RuntimeException("Bad file " + file.getName());
            }
        }
        Object[][] cases = new Object[testCases.size()][1];
        AtomicInteger i = new AtomicInteger();
        testCases.values().forEach(tc -> cases[i.getAndIncrement()] = new Object[]{tc});
        return cases;
    }

    private static String readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        StringBuilder buffer = new StringBuilder();
        while (scanner.hasNextLine()) {
            buffer.append(scanner.nextLine());
            buffer.append("\n");
        }
        return buffer.toString();
    }

    @Parameterized.Parameter()
    public TestCase testCase;

    @Test
    public void parseAndRun() throws IOException {
        Program program = readProgramFromStream(new ByteArrayInputStream(testCase.program.getBytes()));
        Runnable runnable = program.initializeAndPrepareRun();
        runnable.run();
        Assert.assertEquals(testCase.result, (double) program.getVariable(getOutVarId(program)), 0.001);
    }

    @Test
    public void testCompileDecompileAndRun() throws IOException {
        Program program = readProgramFromStream(new ByteArrayInputStream(testCase.program.getBytes()));
        QgCompiler compiler = new QgCompiler(program);
        byte[] compiled = compiler.compile();
        QgDecompiler qgDecompiler = new QgDecompiler(compiled);
        Program decompiledProgram = qgDecompiler.decompile();
        Runnable runnable = decompiledProgram.initializeAndPrepareRun();
        runnable.run();
        Assert.assertEquals(testCase.result, (double) decompiledProgram.getVariable(getOutVarId(program)), 0.001);
    }

    private int getOutVarId(Program program) {
        return ((AssignExpression)((ExpressionStatement)((BlockStatement) program.getInitialisation()).getStatements().get(0)).getExpression()).getAssignTo();
    }

    private static class TestCase {
        public final String name;
        public String program;
        public float result;

        public TestCase(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
