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
	  @RequestMapping(value="/uploadPhoto/{id}" ,method = RequestMethod.GET)
	    public String UploadPhotoProfile() {
	        return "uploadPhoto";
	    }
	  
	  @RequestMapping(value="/profile/{id}?{lastname}{firstname}", method=RequestMethod.GET)
	  public String ProfileUser(){
		  return "profileUser";
	  }
	  @RequestMapping(value="/generateKey/{id}", method=RequestMethod.GET)
	  public String GenerateKey(){
		  return "generateKey";
	  }
	
	  
	  @RequestMapping(value="/userView/{id}/{id}?{firstname}{lastname}", method=RequestMethod.GET)
	  public String userView(){
		  return "userView";
	  }
	  
	  @RequestMapping(value="/searchFriend/{id}", method=RequestMethod.GET)
	  public String searchFriend(){
		  return "searchFriend";
	  }
	
}