package se.lindhen.qrgame;

import se.lindhen.qrgame.parser.ParseException;
import se.lindhen.qrgame.parser.ProgramParser;
import se.lindhen.qrgame.program.Program;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

public class Util {

    public static Program readProgramFromStream(InputStream resourceAsStream) throws IOException {
        try {
            return ProgramParser.parseProgram(resourceAsStream, StandardCharsets.UTF_8);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends FileTestCase> Object[][] readTestCasesFromFiles(Collection<String> directories, String resultFileEnding, Function<String, T> caseCreator) throws URISyntaxException, FileNotFoundException {
        HashMap<String, T> testCases = new HashMap<>();
        for (String directory : directories) {
            File testFolder = new File(QgFileTest.class.getResource(directory).toURI());
            for (File file : testFolder.listFiles()) {
                String[] parts = file.getName().split("\\.");
                String testName = parts[0];
                T testCase = testCases.computeIfAbsent(testName, k -> caseCreator.apply(testName));
                if (parts[1].equals("qg")) {
                    testCase.program = readFile(file);
                } else if (parts[1].equals(resultFileEnding)) {
                    testCase.addResult(readFile(file));
                } else {
                    throw new RuntimeException("Bad file " + file.getName());
                }
            }
        }
        Object[][] cases = new Object[testCases.size()][1];
        AtomicInteger i = new AtomicInteger();
        testCases.values().forEach(tc -> cases[i.getAndIncrement()] = new Object[]{tc});
        return cases;
    }

    public static String readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        StringBuilder buffer = new StringBuilder();
        while (scanner.hasNextLine()) {
            buffer.append(scanner.nextLine());
            buffer.append("\n");
        }
        scanner.close();
        return buffer.toString();
    }

    public static abstract class FileTestCase {

        public String name;
        public String program;

        public abstract void addResult(String fileContent);

    }

}
