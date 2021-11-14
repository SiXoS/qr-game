package se.lindhen.qrgame.cli;

import picocli.CommandLine;

import java.io.IOException;
import java.util.Properties;

public class PropertyVersionProvider implements CommandLine.IVersionProvider {

    private final Properties properties;

    public PropertyVersionProvider() throws IOException {
        properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("project.properties"));
    }

    @Override
    public String[] getVersion() {
        return new String[]{ properties.getProperty("version") };
    }

}
