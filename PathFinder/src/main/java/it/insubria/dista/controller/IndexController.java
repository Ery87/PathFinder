package it.insubria.dista.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class IndexController {

	  @RequestMapping(method = RequestMethod.GET)
	    public String getIndexPage() {
	        return "UserManagement";
	    }
	  
	  @RequestMapping(value="/registration" ,method = RequestMethod.GET)
	    public String Registration() {
	        return "registration";
	    }
	
	 
	
}