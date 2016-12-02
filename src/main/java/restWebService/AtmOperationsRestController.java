package restWebService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hbaseAdo.HbaseDAO;
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
	
	/*I think is better avoid the creation of all objects to serialize with jackson, So
	 * I'll create directement the Json...*/
	@RequestMapping(path = "/getDataFirstTime/", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String  getDataFirstTime(){		
		HbaseDAO hbaseDao = new HbaseDAO(); 
		Map<String,Map<String,String>> allTable = hbaseDao.getAllRowsUnderFamiliys("atm:AtmTotalCash", "Total", "GeoLoc");
		
		StringBuilder stb = new StringBuilder();
		stb.append("[");
		for(String id: allTable.keySet()){
			stb.append("{\"id\":\""+id+"\",");
			stb.append("\"value\":"+allTable.get(id).get("cash")+",");
			stb.append("\"latitude\":\""+allTable.get(id).get("lat")+"\",");
			stb.append("\"longitude\":\""+allTable.get(id).get("lng")+"\"},");
		}
		//remove the last comma
		stb.deleteCharAt(stb.length()-1);
		stb.append("]");		
		
		return stb.toString();
	}


}



