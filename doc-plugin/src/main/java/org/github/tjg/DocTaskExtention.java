package org.github.tjg;

public class DocTaskExtention {

    public static final String DEFAULT_CONFIG_FILE = "dove-swagger.yaml";

    private String configFile = DEFAULT_CONFIG_FILE;

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    @Override
    public String toString() {
        return "DoveExtension{" +
                "configFile='" + configFile + '\'' +
                '}';
    }
}
