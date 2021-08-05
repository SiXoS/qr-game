package se.lindhen.qrgame.cli;

import com.google.zxing.WriterException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import se.lindhen.qrgame.bytecode.QgCompiler;
import se.lindhen.qrgame.parser.ParseException;
import se.lindhen.qrgame.parser.ProgramParser;
import se.lindhen.qrgame.parser.ValidationException;
import se.lindhen.qrgame.program.Program;
import se.lindhen.qrgame.qr.QrCreator;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

@Command(name = "compile", mixinStandardHelpOptions = true)
public class Compile implements Runnable {

    @Option(names = {"-p", "--profile-size"}, description = "Outputs which operations that uses the most bytes.")
    boolean profile;

    @Option(names = {"-f", "--force"}, description = "Overwrite the target file if it exists.")
    boolean overwriteTarget;

    @Option(names = {"-c", "--charset"}, description = "Charset for input file. Default is based on system locale")
    String charset;

    @Option(names = {"-r", "--resolution"}, description = "Resolution of output image in pixels. Default is 1000.", defaultValue = "1000")
    int resolution;

    @Parameters(index = "0", paramLabel = "<qg code path>", arity = "1", description = "Path to qg code")
    String codeFileName;

    @Parameters(index = "1", paramLabel = "<output png path>", arity = "1", description = "Path to output image. Format is PNG.")
    String imageFileName;

    @Override
    public void run() {
        checkTargetDoesNotExist();
        Program program = parseProgram();
        byte[] compiledCode = compile(program);
        generateQrCode(compiledCode);
    }

    private void checkTargetDoesNotExist() {
        File outputFile = new File(imageFileName);
        if (outputFile.exists()) {
            if (overwriteTarget) {
                System.out.println(String.format("Target file '%s' already exists. Will overwrite.", imageFileName));
            } else {
                throw new CliException(String.format("Target file '%s' already exists. Specify '-f' to overwrite", imageFileName), CliErrorCode.TARGET_EXISTS);
            }
        }
        File parent = new File(Paths.get(outputFile.getAbsolutePath())
                .normalize()
                .getParent()
                .toString());
        if (!parent.exists()) {
            throw new CliException(String.format("Parent directory '%s' to target file '%s' does not exist", parent.getAbsolutePath(), outputFile.getAbsolutePath()), CliErrorCode.TARGET_DIR_NOT_FOUND);
        }
        if (!parent.canWrite()) {
            throw new CliException(String.format("Cannot write to target file '%s'. Permission denied.", imageFileName), CliErrorCode.TARGET_FILE_PERMISSION_DENIED);
        }
    }

    private Program parseProgram() {
        File file = new File(codeFileName);
        if (file.exists() && !file.canRead()) {
            throw new CliException(String.format("No read access for '%s'", codeFileName), CliErrorCode.SOURCE_FILE_PERMISSION_DENIED);
        }
        Charset charset = this.charset == null ? Charset.defaultCharset() : Charset.forName(this.charset);
        try(InputStream codeInputStream = new FileInputStream(file)) {
            return ProgramParser.parseProgram(codeInputStream, charset);
        } catch (FileNotFoundException e) {
            throw new CliException(String.format("File '%s' not found.", codeFileName), e, CliErrorCode.SOURCE_NOT_FOUND);
        } catch (IOException e) {
            throw new CliException(String.format("Failed to read input file: '%s'", codeFileName), e, CliErrorCode.UNKNOWN);
        } catch (IllegalCharsetNameException | UnsupportedCharsetException e) {
            throw new CliException(String.format("Could not load charset '%s'. Got error: '%s'", charset, e.getMessage()), e, CliErrorCode.ILLEGAL_CHARSET);
        } catch (ParseException e) {
            throw new CliException(String.format("Parsing failed: %s%s", System.lineSeparator(), e.getMessage()), e, CliErrorCode.PARSE_FAILED);
        } catch (ValidationException e) {
            throw new CliException(String.format("Validation failed: %s%s", System.lineSeparator(), e.getMessage()), e, CliErrorCode.VALIDATE_FAILED);
        }
    }

    private byte[] compile(Program program) {
        QgCompiler qgCompiler = new QgCompiler(program);
        byte[] buffer = qgCompiler.compile();
        int bytesWritten = qgCompiler.getBytesWritten();
        System.out.println(String.format("Total bytes in compiled code: %d. Max: %d", bytesWritten, QrCreator.BYTES_IN_QR_CODE));
        if (profile) {
            System.out.println("Option --profile is set. Bits used by different language features:");
            System.out.println(qgCompiler.getContextualBitsWritten()
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .map(entry -> String.format("\t%s: %d", entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining(System.lineSeparator())));
        }
        if (bytesWritten > QrCreator.BYTES_IN_QR_CODE) {
            throw new CliException("Size of compiled code exceeds max storage in the largest QR code.", CliErrorCode.BYTECODE_TOO_LARGE);
        }
        return buffer;
    }

    private void generateQrCode(byte[] compiledCode) {
        try {
            QrCreator.createQrImage(compiledCode, new File(imageFileName).toPath(), resolution);
        } catch (IOException | WriterException e) {
            throw new CliException(String.format("Could not create output image: %s", e.getMessage()), e, CliErrorCode.UNKNOWN);
        }
    }

}
