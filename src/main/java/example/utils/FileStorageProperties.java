package example.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String dumpDir;

    public String getDumpDir() {
        return dumpDir;
    }

    public void setDumpDir(String dumpDir) {
        this.dumpDir = dumpDir;
    }
}