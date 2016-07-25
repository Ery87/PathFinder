package it.insubria.dista.controller;
 
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.mysql.jdbc.util.Base64Decoder;

import it.insubria.dista.Build;
import it.insubria.dista.ControllerPath;
import org.springframework.http.converter.json.*;

@Controller
@RestController
public class HelloWorldRestController {
 
 
 
    
 
    
    //-------------------Retrieve Single User--------------------------------------------------------
     
	@RequestMapping(value = { "/profile/" }, method = RequestMethod.POST)
	@ResponseBody
	public String getUser(@RequestBody Integer user) throws UnsupportedEncodingException {

		Build.buildGraph();
		return "{\"success\":1}";
    }
 
	
     
    @RequestMapping(value="/eval/",method=RequestMethod.POST)
    
    public String  eval(@RequestBody String r) throws NumberFormatException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException, ExecutionException, SQLException{
   
   	String result;
   	result=ControllerPath.execution(r);
   	System.out.println(result);
   return "{\"response\":\""+result+"\"}";
   
    	
		
    }
  
 
    
     
   
}