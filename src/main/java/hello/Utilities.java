package hello;

public class Utilities {
    fetchInputClient client;
    fetchInputConfig config;
    ConfPrepare confPrepare;

    public Utilities(fetchInputClient client, fetchInputConfig config) {
        this.client = client;
        this.config = config;
        confPrepare = new ConfPrepare(client, config);
    }

    public String prepare() {
        String temp[] = this.client.getFilepath().split("/");
        String filename = temp[temp.length - 1];
        String cmd[] = new String[3];
        cmd[0] = "bash";
        cmd[1] = "-c";
        cmd[2] = " screen -S " + this.config.getScreenInstanceName() + " -t start -d -m";
        System.out.println("Executing Command: " + cmd[2]);
        executeCmd(cmd);
        if (client.getIsLocalFilePath()) {
            cmd[2] = "cp " + this.client.getFilepath() + " " +  this.config.getLocalConfDir() + filename + " > " + this.config.getCmdLogDir() +"copying_file_locally.log";
            System.out.println("Executing Command: " + cmd[2]);
            executeCmd(cmd);
        } else {
            //Need to create this command
            cmd[2] = "scp " + this.config.getScpSource() + ":" + this.client.getFilepath() + " " + this.config.getScpDest() + ":" + this.config.getLocalConfDir() + filename;
            System.out.println("Executing Command: " + cmd[2]);
            executeCmd(cmd);
            /*cmd = "screen -S " + this.config.getScreenInstanceName() + " -X screen -t copying_conf_files_remotely";
            System.out.println("Executing Command: " + cmd);
            executeCmd(cmd);
            cmd = "screen -S " + this.config.getScreenInstanceName() + " -p copying_conf_files_remotely -X stuff $'" + "scp " + this.config.getScpSource() + ":" + this.client.getFilepath() + " " + this.config.getScpDest() + ":" + this.config.getLocalConfDir() + filename + "\n'";
            System.out.println("Executing Command: " + cmd);
            executeCmd(cmd);*/
        }
        String result[] = confPrepare.finalConf(this.config.getLocalConfDir() + filename);
        if (!result[0].equals("Logstash Conf File Created")) {
            return result[0];
        }
        cmd[2] = "screen -S " + this.config.getScreenInstanceName() + " -X screen -t logstash";
        System.out.println("Executing Command: " + cmd[2]);
        executeCmd(cmd);
        cmd[2] = "screen -S " + this.config.getScreenInstanceName() + " -p logstash -X stuff $'" + this.config.getLogstashDirPath() + "./logstash -f " + result[1] + " --config.reload.automatic" + "\n'";
        System.out.println("Executing Command: " + cmd[2]);
        executeCmd(cmd);
        result = confPrepare.filebeatYmlPrep();
        if (!result[0].equals("Filebeat Yml File Created")) {
            return result[0];
        }
        cmd[2] = "screen -S " + this.config.getScreenInstanceName() + " -X screen -t filebeat";
        System.out.println("Executing Command: " + cmd[2]);
        executeCmd(cmd);
        cmd[2] = "screen -S " + this.config.getScreenInstanceName() + " -p filebeat -X stuff $'rm -rf /usr/share/filebeat/bin/data/registry\n'";
        System.out.println("Executing Command: " + cmd[2]);
        executeCmd(cmd);
        cmd[2] = "screen -S " + this.config.getScreenInstanceName() + " -p filebeat -X stuff $'" + "/usr/share/filebeat/bin/./filebeat -e -c " + result[1] + " -d \"publish\"" + "\n'";
        System.out.println("Executing Command: " + cmd[2]);
        executeCmd(cmd);
        return "Ingestion Initiated!!!!!!!";
    }


    public void executeCmd(String[] cmd) {
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Executing Command: " + cmd[2]);
        }
    }
}
