package se.lindhen.qrgame.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "qrgame", mixinStandardHelpOptions = true, subcommands = {Compile.class}, versionProvider = PropertyVersionProvider.class)
public class QrGame {

    public static void main(String[] args) {
        System.exit(execute(args));
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    static int execute(String[] args) {
        return new CommandLine(new QrGame())
                .setExecutionExceptionHandler(new ExceptionHandler())
                .execute(args);
    }

}
