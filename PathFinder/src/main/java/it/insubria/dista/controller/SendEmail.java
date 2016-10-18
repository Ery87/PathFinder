package it.insubria.dista.controller;


import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;   

import java.util.*;
import javax.mail.internet.*;


public class SendEmail {
	
	
	private String host="smtp.googlemail.com";
	
	
	private String from="dsn.project.p2p@gmail.com";
	
	private String user="dsn.project.p2p@gmail.com";
	
	private String pass="dsnprojectp2p";
	
	
	
  private String ToAddress; //destinatario
  private MimeMessage msg = null;
  private Session session = null;
  
  public SendEmail(String toAddress) {
	  ToAddress = toAddress;
    try {      

    	
      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", "smtp.gmail.com");
      props.put("mail.smtp.port", "587");

      //Get session
      session = Session.getDefaultInstance(props, null);
      session.setDebug(true);
      session.setPasswordAuthentication(new URLName("smtp",host,25,"INBOX",user,pass), new PasswordAuthentication(user,pass));

      //Define message
      msg = new MimeMessage(session);
      //Set the from address
   
      msg.setFrom(new InternetAddress(from));
      //Set the to address
      msg.addRecipient(Message.RecipientType.TO, new
InternetAddress(ToAddress));
    }
    catch (MessagingException e) {
      System.out.println(e);
    }
  } 
  
  public void send() {
	//Send message
      Transport tr;
	try {
		tr = session.getTransport("smtp");
		tr.connect(host, user, pass);
		msg.saveChanges();
		tr.sendMessage(msg, msg.getAllRecipients());
		tr.close();
	} catch (NoSuchProviderException e) {
		e.printStackTrace();
	}catch (MessagingException e) {
		e.printStackTrace();
	}
  }
  
  public void setText(String txt) {
	 try {
		msg.setText(txt);
	} catch (MessagingException e) {
		e.printStackTrace();
	}
  }
  
  public void setSubject(String s) {
	  try {
		msg.setSubject(s);
	} catch (MessagingException e) {
		e.printStackTrace();
	}
  }
  

}