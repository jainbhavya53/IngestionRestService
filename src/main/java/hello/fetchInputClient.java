package hello;

import java.io.FileInputStream;
import java.util.Properties;

public class fetchInputClient {
    String templateType;
    String esHosts;
    String indexName;
    boolean isLocalFilePath;
    String localLogPath;
    String filePath;

    public String getTemplateType() {
        return this.templateType;
    }

    public void setTemplateType(String templateType){
        this.templateType = templateType;
    }

    public String getEsHosts() {
        return this.esHosts;
    }

    public void setEsHosts(String esHosts){
        this.esHosts = esHosts;
    }

    public String getIndexName() {
        return this.indexName;
    }

    public void setIndexName(String indexName){
        this.indexName = indexName;
    }

    public String getLocalLogPath() {
        return this.localLogPath;
    }

    public void setLocalLogPath(String localLogPath){
        this.localLogPath = localLogPath;
    }

    public String getFilepath() {
        return this.filePath;
    }

    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public boolean getIsLocalFilePath() {
        return this.isLocalFilePath;
    }

    public void setIsLocalFilePath(boolean isLocalFilePath){
        this.isLocalFilePath = isLocalFilePath;
    }
}

