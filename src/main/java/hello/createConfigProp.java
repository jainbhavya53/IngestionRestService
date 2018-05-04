package hello;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

public class createConfigProp {
    public createConfigProp(){
        try {
            File file = new File("config.properties");
            if(!file.exists()){
                Properties properties = new Properties();
                properties.setProperty("PORT","5043");
                properties.setProperty("LOCAL_CONF_DIRECTORY","/home/bhavya/Downloads/generatedTempFiles/");
                properties.setProperty("FILEBEAT_DIR_PATH","/etc/filebeat/");
                properties.setProperty("LOGSTASH_DIR_PATH","/home/bhavya/Downloads/logstash-6.1.2/bin/");
                properties.setProperty("LOCAL_TEMPALTE_PATH","/home/application/bhavya/templates/");
                properties.setProperty("PATTERNS_DIR","/home/application/bhavya/patterns/");
                properties.setProperty("SCP_SOURCE","bhavyajain@192.168.109.245");
                properties.setProperty("SCP_DEST","root@192.168.133.187");
                properties.setProperty("CMD_LOG_DIR","/home/application/cmd_output_log/");

                FileOutputStream fileOut = new FileOutputStream(file);
                properties.store(fileOut, "Config parameters");
                fileOut.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
