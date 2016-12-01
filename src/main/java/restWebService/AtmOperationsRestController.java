package restWebService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import model.DataModel;
import model.SingletonVariablesShare;

import org.springframework.http.MediaType;

@CrossOrigin
@RestController
public class AtmOperationsRestController{ 
	
	
	@RequestMapping(path = "/version/", method = RequestMethod.GET, produces=MediaType.ALL_VALUE)
	public String getVersion(){	    
		
		return "0.0.1\n";
	}
	
	@RequestMapping(path = "/getData/", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<DataModel> getData(){
		
		List<DataModel> lastBatch = SingletonVariablesShare.INSTANCE.getLastMessagesMicroBatch();
	    
		
		return lastBatch;
	}


}
