package se.lindhen.qrgame.cli;

import picocli.CommandLine;

public class ExceptionHandler implements CommandLine.IExecutionExceptionHandler {

    @Override
    public int handleExecutionException(Exception exception, CommandLine commandLine, CommandLine.ParseResult parseResult) throws Exception {
        if (exception instanceof CliException) {
            System.err.println(exception.getMessage());
            return ((CliException) exception).getErrorCode().getExitCode();
        } else {
            System.err.println("Unexpected exception");
            exception.printStackTrace();
            return CliErrorCode.UNKNOWN.getExitCode();
        }
    }

}
