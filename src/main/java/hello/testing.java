package hello;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logstash/ingestion/")
public class testing {
    fetchInputClient client;
    fetchInputConfig config;
    Utilities utility;
    createConfigProp configProp;
    Status status;

    public testing()
    {
        System.out.println("testing object created");
        configProp = new createConfigProp();
    }

    @RequestMapping(value = "new", method = RequestMethod.POST)
    ResponseEntity<String> newIngestion(@RequestBody fetchInputClient input) {
        this.client = input;
        this.config = new fetchInputConfig();
        this.utility = new Utilities(this.client, this.config);
        String result = utility.prepare();
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "status", method = RequestMethod.GET)
    ResponseEntity<String> getIngestionStatus()
    {
        String result;
        if(this.client == null || this.config == null)
        {
            result = "Ingestion Not Yet Started";
        }
        else{
            this.status = new Status(this.client,this.config);
            result = this.status.checkIngestionStatus();
        }

        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "kill", method = RequestMethod.GET)
    ResponseEntity<String> killLogstashFilebeat()
    {
        String result;
        if(this.status == null)
        {
            result = "Ingestion Not Yet Started,therefore No Running Instance of Logstash and Filebeat";
        }
        else{
            result = this.status.stop_ingestion();
        }

        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "testing", method = RequestMethod.GET)
    ResponseEntity<String> testingRestCall() {
        config = new fetchInputConfig();
        System.out.println(this.config.getPort());
        return new ResponseEntity<String>(this.config.getScpSource(), HttpStatus.OK);
    }
}