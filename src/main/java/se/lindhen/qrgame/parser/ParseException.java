package se.lindhen.qrgame.parser;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ParseException extends Exception {

    public ParseException(ArrayList<String> errorMessages) {
        super("Could not parse program, got the following errors:" + System.lineSeparator() +
                errorMessages.stream().collect(Collectors.joining(System.lineSeparator())));
    }

}
