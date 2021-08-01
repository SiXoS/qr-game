package se.lindhen.qrgame.cli;

import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class CliTest {

    private static Path tempDir;
    private static Path codeDir;

    @BeforeClass
    public static void createDirs() throws IOException, URISyntaxException {
        tempDir = Files.createTempDirectory("qr-game-tests");
        codeDir = Paths.get(CliTest.class.getResource("/tests/cli").toURI());
    }

    @AfterClass
    public static void removeTempDir() {
        new File(CliTest.tempDir.toAbsolutePath().toString()).delete();
    }

    @After
    public void clearTempDir() {
        File tempDir = new File(CliTest.tempDir.toAbsolutePath().toString());
        File[] children = tempDir.listFiles();
        if (children != null) {
            for (File child : children) {
                child.delete();
            }
        }
    }

    @Test
    public void testCompile() {
        Path outputPath = Paths.get(tempDir.toAbsolutePath().toString(), "simple.png");
        int exitCode = QrGame.execute(new String[]{"compile", testPath("simple.qg"), outputPath.toString()});
        assertEquals(0, exitCode);
        File outputFile = new File(outputPath.toString());
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 500);
    }

    @Test
    public void testFailOnExistingTarget() {
        Path outputPath = Paths.get(tempDir.toAbsolutePath().toString(), "existingTarget.png");
        int exitCode = QrGame.execute(new String[]{"compile", testPath("simple.qg"), outputPath.toString()});
        assertEquals(0, exitCode);
        exitCode = QrGame.execute(new String[]{"compile", testPath("simple.qg"), outputPath.toString()});
        assertEquals(CliErrorCode.TARGET_EXISTS.getExitCode(), exitCode);
    }

    @Test
    public void testOverwriteExistingTargetWithForce() {
        Path outputPath = Paths.get(tempDir.toAbsolutePath().toString(), "existingTargetWithForce.png");
        int exitCode = QrGame.execute(new String[]{"compile", testPath("simple.qg"), outputPath.toString()});
        assertEquals(0, exitCode);
        exitCode = QrGame.execute(new String[]{"compile", "-f", testPath("simple.qg"), outputPath.toString()});
        assertEquals(0, exitCode);
    }

    @Test
    public void testSourceNotFound() {
        Path outputPath = Paths.get(tempDir.toAbsolutePath().toString(), "404.png");
        int exitCode = QrGame.execute(new String[]{"compile", testPath("blahblah.qg"), outputPath.toString()});
        assertEquals(CliErrorCode.SOURCE_NOT_FOUND.getExitCode(), exitCode);
        assertFalse(new File(outputPath.toString()).exists());
    }

    @Test
    public void testParseError() {
        Path outputPath = Paths.get(tempDir.toAbsolutePath().toString(), "parseError.png");
        int exitCode = QrGame.execute(new String[]{"compile", testPath("invalid.qg"), outputPath.toString()});
        assertEquals(CliErrorCode.PARSE_FAILED.getExitCode(), exitCode);
        assertFalse(new File(outputPath.toString()).exists());
    }

    @Test
    public void testTypeError() {
        Path outputPath = Paths.get(tempDir.toAbsolutePath().toString(), "typeError.png");
        int exitCode = QrGame.execute(new String[]{"compile", testPath("typeError.qg"), outputPath.toString()});
        assertEquals(CliErrorCode.VALIDATE_FAILED.getExitCode(), exitCode);
        assertFalse(new File(outputPath.toString()).exists());
    }

    private String testPath(String testFile) {
        return Paths.get(codeDir.toAbsolutePath().toString(), testFile).toString();
    }

}
