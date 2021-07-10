package se.lindhen.qrgame.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "qrgame", mixinStandardHelpOptions = true, subcommands = {Compile.class})
public class QrGame {

    @SuppressWarnings("InstantiationOfUtilityClass")
    public static void main(String[] args) {
        int exitCode = new CommandLine(new QrGame()).execute(args);
        System.exit(exitCode);
    }

}
