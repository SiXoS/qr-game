package se.lindhen.qrgame;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import se.lindhen.qrgame.parser.ParseException;
import se.lindhen.qrgame.parser.ProgramParser;
import se.lindhen.qrgame.parser.ValidationException;
import se.lindhen.qrgame.parser.ValidationResult;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class QgCompileErrorTest {

    @Parameterized.Parameters(name = "{0}")
    public static Object[][] testCases() throws FileNotFoundException, URISyntaxException {
        return Util.readTestCasesFromFiles(Arrays.asList("/tests/typeError"), "error", ErrorTestCase::new);
    }

    @Parameterized.Parameter()
    public ErrorTestCase testCase;

    @Test
    public void test() throws IOException, ParseException {
        try {
            ProgramParser.parseProgram(new ByteArrayInputStream(testCase.program.getBytes()), StandardCharsets.UTF_8);
        } catch (ValidationException e) {
            List<String> originalErrors = e.getValidationResult().getMessages();
            List<String> errorMessages = new LinkedList<>(originalErrors);
            for (Integer errorLine : testCase.errorLines) {
                boolean hasErrorOnLine = false;
                Iterator<String> messageIterator = errorMessages.iterator();
                while (messageIterator.hasNext()) {
                    String message = messageIterator.next();
                    if (message.contains("Error at " + errorLine + ":")) {
                        messageIterator.remove();
                        hasErrorOnLine = true;
                    }
                }
                if (!hasErrorOnLine) {
                    Assert.fail("Expected error on line " + errorLine + " but didn't find it in errors: " + System.lineSeparator() +
                            originalErrors.stream().collect(Collectors.joining(System.lineSeparator())));
                }
            }
            if (!errorMessages.isEmpty()) {
                Assert.fail("Unexpected errors: " + errorMessages.stream().collect(Collectors.joining(System.lineSeparator())));
            }
        }
    }

    private static class ErrorTestCase extends Util.FileTestCase {

        private List<Integer> errorLines = new ArrayList<>();

        public ErrorTestCase(String name) {
            this.name = name;
        }

        @Override
        public void addResult(String fileContent) {
            for (String line : fileContent.split("\r\n|\r|\n")) {
                errorLines.add(Integer.parseInt(line.trim()));
            }
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
