package se.lindhen.qrgame;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import se.lindhen.qrgame.bytecode.QgDecompiler;
import se.lindhen.qrgame.program.GameLoop;
import se.lindhen.qrgame.program.InputManager;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.program.expressions.AssignExpression;
import se.lindhen.qrgame.program.statements.BlockStatement;
import se.lindhen.qrgame.program.statements.ExpressionStatement;
import se.lindhen.qrgame.qr.QrCreator;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(Parameterized.class)
public class BackwardsCompatibilityTest {

    @Parameterized.Parameters(name = "{0}")
    public static Object[][] getTestCases() throws URISyntaxException, FileNotFoundException {
        HashMap<String, BackwardsCompatibilityTestCase> testCases = new HashMap<>();
        Util.readAllFiles(Arrays.asList("/tests/backwardsCompatibility"), (fileName, fileEnding, file) -> {
            BackwardsCompatibilityTestCase testCase = testCases.computeIfAbsent(fileName, k -> new BackwardsCompatibilityTestCase(fileName));
            if (fileEnding.equals("png")) {
                testCase.setQrCode(file);
            } else if (fileEnding.equals("out")) {
                testCase.setResult(Double.parseDouble(Util.readFile(file)));
            } else {
                throw new RuntimeException("Bad file " + file.getName());
            }
        });
        Object[][] cases = new Object[testCases.size()][1];
        AtomicInteger i = new AtomicInteger();
        testCases.values().forEach(tc -> cases[i.getAndIncrement()] = new Object[]{tc});
        return cases;
    }

    @Parameterized.Parameter()
    public BackwardsCompatibilityTestCase testCase;

    @Test
    public void testDecompileAndRun() throws IOException {
        byte[] compiled = QrCreator.readQrImage(testCase.qrResource);
        QgDecompiler qgDecompiler = new QgDecompiler(compiled);
        Program decompiledProgram = qgDecompiler.decompile();
        GameLoop iteration = decompiledProgram.initializeAndPrepareRun();
        decompiledProgram.getInputManager().triggerButton(InputManager.Input.LEFT_BOTTOM, true);
        iteration.run(100);
        Assert.assertEquals(testCase.result, (double) decompiledProgram.getVariable(getOutVarId(decompiledProgram)), 0.001);
    }

    private int getOutVarId(Program program) {
        return ((AssignExpression)((ExpressionStatement)((BlockStatement) program.getInitialisation()).getStatements().get(0)).getExpression()).getAssignTo();
    }

    private static class BackwardsCompatibilityTestCase {

        private final String name;
        private File qrResource;
        private double result;

        public BackwardsCompatibilityTestCase(String name) {
            this.name = name;
        }

        public void setQrCode(File qrResource) {
            this.qrResource = qrResource;
        }

        public void setResult(double result) {
            this.result = result;
        }
    }
}
