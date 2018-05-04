package hello;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Status {

    boolean ingestionComp;
    String status;
    fetchInputConfig config;
    fetchInputClient client;

    public Status(fetchInputClient client,fetchInputConfig config){
        this.ingestionComp = true;
        this.status = "Ingestion Not Yet Started";
        this.client = client;
        this.config = config;
    }

    public String checkIngestionStatus(){
            StringBuilder sb = new StringBuilder();
            String[] cmd = {"bash","-c",""};
            cmd[2] = "awk '!/^#/ && !/^$/{c++}END{print c}' " + this.client.getLocalLogPath();
            System.out.println("Executing Command: " + cmd[2]);
            sb.append("Number of Records in log file => " + executeCmd(cmd));
            sb.append("\n");
            cmd[2] = "curl -XGET \"http://" + this.client.getEsHosts().split(",")[0] +"/" + this.client.getIndexName() + "*/_count\" | awk -F ',' '{print $1}' | awk -F ':' '{print $2}'";
            System.out.println("Executing Command: " + cmd[2]);
            sb.append("Number of Records Ingested So Far => " + executeCmd(cmd));
            sb.append("\n");
            return sb.toString();
    }

    public String stop_ingestion(){
        StringBuilder sb = new StringBuilder();
        String[] cmd = {"bash","-c",""};

        //cmd[2] = "ps -ef | grep [S]CREEN | awk -F ' ' '{print $2}'";
        String temp_screen_inst = "[" + this.config.getScreenInstanceName().substring(0,1) + "]" + this.config.getScreenInstanceName().substring(1);
        cmd[2] = "ps -ef | grep -w [S]CREEN | grep -w " + temp_screen_inst + " | awk -F ' ' '{print $2}'";
        System.out.println("Executing Command: " + cmd[2]);
        String output = executeCmd(cmd);
        sb.append("PID of Screen Session => " + output);
        sb.append("\n");

        String[] temp = output.split("\n");
        for(int i = 0;i<temp.length;i++)
        {
            cmd[2] = "kill -9 " + temp[i];
            System.out.println("Executing Command: " + cmd[2]);
            sb.append("Output of kill -9 => " + executeCmd(cmd));
            sb.append("\n");
        }
        //System.out.println(temp[temp.length-1]);

        cmd[2] = "screen -ls";
        System.out.println("Executing Command: " + cmd[2]);
        sb.append("Output of screen -ls => " + executeCmd(cmd));
        sb.append("\n");

        cmd[2] = "screen -wipe";
        System.out.println("Executing Command: " + cmd[2]);
        sb.append("Clearing all ingestion screen sessions => " + executeCmd(cmd));
        sb.append("\n");

        cmd[2] = "screen -ls";
        System.out.println("Executing Command: " + cmd[2]);
        sb.append("Output of screen -ls after wiping ingestion screen instances => " + executeCmd(cmd));
        sb.append("\n");

        cmd[2] = "ps -ef | grep [l]ogstash | awk -F ' ' '{print $2}'";
        System.out.println("Executing Command: " + cmd[2]);
        output = executeCmd(cmd);
        sb.append("Stopping Logtstash => " + output);
        sb.append("\n");

        temp = output.split("\n");
        for(int i = 0;i<temp.length;i++) {
            cmd[2] = "kill -9 " + temp[i];
            System.out.println("Executing Command: " + cmd[2]);
            sb.append("Output of kill -9 => " + executeCmd(cmd));
            sb.append("\n");
        }
        //System.out.println(temp[temp.length-1]);

        return sb.toString();
    }


    public String executeCmd(String[] cmd)
    {
        String line;
        Process p;
        StringBuilder st = new StringBuilder();
        try {
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            line = br.readLine();
            //int count = 1;
            while(line != null)
            {
                st.append(line + "\n");
                System.out.println(line);
                //count++;
                line = br.readLine();
            }
            br.close();
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error executing command: " + cmd[2]);
            return "Error Executing Command " + cmd[2];
        }
        return st.toString();
    }
}
