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
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

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
        readAllFiles(directories, (fileName, fileEnding, file) -> {
            T testCase = testCases.computeIfAbsent(fileName, k -> caseCreator.apply(fileName));
            if (fileEnding.equals("qg")) {
                testCase.program = readFile(file);
            } else if (fileEnding.equals(resultFileEnding)) {
                testCase.setResult(readFile(file));
            } else {
                throw new RuntimeException("Bad file " + file.getName());
            }
        });
        Object[][] cases = new Object[testCases.size()][1];
        AtomicInteger i = new AtomicInteger();
        testCases.values().forEach(tc -> cases[i.getAndIncrement()] = new Object[]{tc});
        return cases;
    }

    public static void readAllFiles(Collection<String> directories, FileReader fileReader) throws URISyntaxException, FileNotFoundException {
        for (String directory : directories) {
            File testFolder = new File(QgFileTest.class.getResource(directory).toURI());
            for (File file : testFolder.listFiles()) {
                if (file.isDirectory()) {
                    continue;
                }
                String[] parts = file.getName().split("\\.");
                String fileName = parts[0];
                String fileEnding = parts[1];
                if (fileEnding.equals("md")) {
                    continue;
                }
                fileReader.fileRead(fileName, fileEnding, file);
            }
        }
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

        public FileTestCase(String name) {
            this.name = name;
        }

        public abstract void setResult(String fileContent);

        @Override
        public String toString() {
            return name;
        }
    }

    public interface FileReader {

        void fileRead(String fileName, String fileEnding, File file) throws FileNotFoundException;

    }

}
