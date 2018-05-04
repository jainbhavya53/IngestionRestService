package hello;

import java.io.FileInputStream;
import java.util.Properties;

public class fetchInputConfig {
    Properties prop;
    FileInputStream inputStream;
    final String screenInstanceName = "ingestionScreenInst";

    public fetchInputConfig() {
        try {
            inputStream = new FileInputStream("config.properties");
            prop = new Properties();
            prop.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getScreenInstanceName(){
        return this.screenInstanceName;
    }

    public String getPort() {
        return prop.getProperty("PORT");
    }

    public String getLocalConfDir() {
        return prop.getProperty("LOCAL_CONF_DIRECTORY");
    }

    public String getLogstashDirPath() {
        return prop.getProperty("LOGSTASH_DIR_PATH");
    }

    public String getLocalTempDir() {
        return prop.getProperty("LOCAL_TEMPALTE_PATH");
    }

    public String getPatternsDir() {
        return prop.getProperty("PATTERNS_DIR");
    }

    public String getScpSource() {
        return prop.getProperty("SCP_SOURCE");
    }

    public String getScpDest() {
        return prop.getProperty("SCP_DEST");
    }

    public String getCmdLogDir() {
        return prop.getProperty("CMD_LOG_DIR");
    }
}
