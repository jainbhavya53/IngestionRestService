package hello;

import java.io.*;

public class ConfPrepare {
    fetchInputClient client;
    fetchInputConfig config;

    public ConfPrepare(fetchInputClient client, fetchInputConfig config) {
        this.client = client;
        this.config = config;
    }

    public String[] finalConf(String filePath) {

        String[] template = templatePath();
        String num[];
        String output_filepath = null;
        //Input File
        try {
            System.out.println("Preparing Logstash Conf File");
            File fileObjInput = new File(filePath);
            String temp[] = fileObjInput.getPath().split("/");
            System.out.println("input file object: " + fileObjInput.getAbsolutePath());
            //Input File
            BufferedReader br = new BufferedReader(new FileReader(fileObjInput));
            String filename = temp[temp.length - 1];
            System.out.println("Filename: " + filename);
            //Output File
            File fileObjOutput = new File(fileObjInput.getAbsolutePath().substring(0, fileObjInput.getAbsolutePath().length() - filename.length()) + filename.substring(0, filename.length() - 5) + "_tmp.conf");
            System.out.println(fileObjOutput.getAbsolutePath());
            if (fileObjOutput.exists()) {
                fileObjOutput.delete();
            }
            output_filepath = fileObjOutput.getAbsolutePath();
            PrintWriter out = new PrintWriter(fileObjOutput);
            String line = br.readLine();
            while (line != null) {
                if (line.contains("DATASETNAME")) {
                    line = line.replaceFirst("DATASETNAME", this.client.getIndexName());
                } else if (line.contains("LOGSTASH_PORT")) {
                    line = line.replaceFirst("LOGSTASH_PORT", this.config.getPort());
                } else if (line.contains("patterns_dir =>")) {
                    line = line.replaceFirst(line.substring(line.indexOf("patterns_dir =>")), "patterns_dir => \"" + this.config.getPatternsDir() + "\"");
                } else if (line.contains("TEMPLATE_TYPE")) {
                    if (template[0].equals("Template Created")) {
                        line = line.replaceFirst("TEMPLATE_TYPE", template[1]);
                    } else {
                        num = new String[1];
                        num[0] = "Error Creating Template";
                        return num;
                    }
                } else if (line.contains("ELASTICSEARCH_HOSTS")) {
                    String es_hosts_list = this.client.getEsHosts().replaceAll(",","\",\"");
                    line = line.replaceFirst("ELASTICSEARCH_HOSTS", es_hosts_list);
                }
                out.println(line);
                line = br.readLine();
            }
            out.close();
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            num = new String[1];
            num[0] = "Error";
            return num;
        }
        num = new String[2];
        num[0] = "Logstash Conf File Created";
        num[1] = output_filepath;
        return num;
    }

    //valid values for template types are small,medium,large
    public String[] templatePath() {
        String temp = this.config.getLocalTempDir() + this.client.getTemplateType() + ".json";
        String output_filePath = this.config.getLocalConfDir() + this.client.getTemplateType() + "_tmp.json";
        File fileObjInput = new File(temp);
        File fileObjOutput = new File(output_filePath);
        try {
            System.out.println("Preparing Template File");
            BufferedReader br = new BufferedReader(new FileReader(fileObjInput));
            if (fileObjOutput.exists()) {
                fileObjOutput.delete();
            }
            PrintWriter out = new PrintWriter(fileObjOutput);
            String line = br.readLine();
            while (line != null) {
                if (line.contains("DATASETNAME")) {
                    line = line.replaceFirst("DATASETNAME", this.client.getIndexName());
                }
                out.println(line);
                line = br.readLine();
            }
            out.close();
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            String[] tmp = new String[1];
            tmp[0] = "Template Not Found";
            return tmp;
        }
        String[] tmp = new String[2];
        tmp[0] = "Template Created";
        tmp[1] = output_filePath;
        return tmp;
    }

    //To prepare
    public String[] filebeatYmlPrep() {
        String num[] = new String[2];
        try {
            System.out.println("Preparing Filebeat Yml File");
            File fileObjOutput = new File(this.config.getLocalConfDir() + "filebeat_tmp.yml");
            if (fileObjOutput.exists()) {
                fileObjOutput.delete();
            }
            PrintWriter out = new PrintWriter(fileObjOutput);
            out.println("filebeat.prospectors:");
            out.println("- input_type: log");
            out.println("  paths:");
            out.println("     - " + this.client.getLocalLogPath());
            out.println("  exclude_lines: [\"^#\"]");
            out.println("");
            out.println("output.logstash:");
            out.println("  hosts: [\"localhost:" + this.config.getPort() + "\"]");
            out.close();
            num[1] = fileObjOutput.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            num[0] = "Filebeat Yml File could not be Created";
            return num;
        }
        num[0] = "Filebeat Yml File Created";
        return num;
    }
}
