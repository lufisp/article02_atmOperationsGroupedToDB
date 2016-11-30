package restWebService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

@CrossOrigin
@RestController
public class AtmOperationsRestController{ 
	
	@RequestMapping(path = "/version/", method = RequestMethod.GET, produces=MediaType.ALL_VALUE)
	public String getVersion(){
		return "0.0.1";
	}


}
