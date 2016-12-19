package it.insubria.dista.controller;
 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Base64;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import it.insubria.dista.Exceptions.ExistingUserExcepion;
import it.insubria.dista.PathFinderService.PathFinderService;
import it.insubria.dista.PathFinderService.UserData;
import it.insubria.dista.Polynomials.Polynomial;



@Controller
@RestController
public class HelloWorldRestController {
 

	
	@Value("${modulus}")
	private BigInteger modulus;
	
	@Value("${private_exponent}")
	private BigInteger private_exponent;
	
	@Value("${public_exponent}")
	private BigInteger public_exponent;
	
	@Value("${modulus_KMS}")
	private BigInteger modulus_KMS;
	

	@Value("${public_exponent_KMS}")
	private BigInteger public_exponent_KMS;
	
	
	private final static ExecutorService executor=Executors.newCachedThreadPool();
  
    //------------------Creation user--------------------------------------------------------

	@RequestMapping(value="/userInsertion",method=RequestMethod.POST)
	public void userInsertion(HttpServletResponse res,@RequestBody String uid) throws IOException, JSONException{
		
		String query;
		PathFinderService PFS=null;
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PFS","root","tomcat@dicom");
			Statement state = connection.createStatement();

			
			ResultSet rs = state.executeQuery("show tables;");
			boolean empty = true;
			while (rs.next()) {
				if (rs.getString("Tables_in_PFS").equals("polynomials")) {
					empty = false;
				}
			}
			
			if (empty) {

				String create = "CREATE TABLE IF NOT EXISTS polynomials (row_id BIGINT NOT NULL AUTO_INCREMENT UNIQUE KEY, uid VARCHAR(10000)";
				for (int i=1; i<=UserData.MAX_DEPTH; i++) {
					create += ", polyLv"+i+" LONGTEXT";
				}
				create += ");";
				
				state.executeUpdate(create);
				PFS = PathFinderService.getInstance();
				connection.close();
			}
			else{
				PFS.restore();
				if(new UserData(new BigInteger(""+uid+"")).exists()){
					throw new ExistingUserExcepion("User already exists");
				}
			}
			
			
			
			new UserData(new BigInteger(""+uid+""), new Polynomial(new BigInteger("1")));
			PrintWriter pw=null;
			
			try{
	          	pw = res.getWriter();    	 	
	        	pw.println("ok");
	        	}catch(Exception ex)
	          	{
	          	pw.println("{");
	          	pw.println("\"successful\": false,");
	          	pw.println("\"message\": \""+ex.getMessage()+"\",");
	          	pw.println("}");
	          	return;
	          	}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
        //-------------------Evaluation directs friendship --------------------------------------------------------

       	@RequestMapping(value="/evaluationFriendship/",method=RequestMethod.POST)
    	public void evaluationFriendship(HttpServletResponse res,HttpServletRequest req,@RequestBody String messageToPFS) throws IOException, JSONException, NoSuchAlgorithmException, InvalidKeySpecException{
       		int evaluation=0;
       		PrintWriter pw=null;
       		RSAPrivateKeySpec spec = new RSAPrivateKeySpec(modulus,private_exponent);
        	KeyFactory factory = KeyFactory.getInstance("RSA");
        	PrivateKey privKey = factory.generatePrivate(spec);
        	
     		Cipher cipher;
     		byte[] dectyptedText = new byte[1];
     		
            try {
              cipher = javax.crypto.Cipher.getInstance("RSA");
              
              byte[] messaggioCifratoBytes = new byte[256];

              BigInteger messaggioCifrato = new BigInteger(messageToPFS, 16);
              if (messaggioCifrato.toByteArray().length > 256) {
                  for (int i=1; i<257; i++) {
                	  messaggioCifratoBytes[i-1] = messaggioCifrato.toByteArray()[i];
                  }
              } else {
            	  messaggioCifratoBytes = messaggioCifrato.toByteArray();
              }
             
              cipher.init(Cipher.DECRYPT_MODE, privKey);
              dectyptedText = cipher.doFinal(messaggioCifratoBytes);
              } catch(NoSuchAlgorithmException e) {
            	  System.out.println(e);
              } catch(NoSuchPaddingException e) { 
            	  System.out.println(e);
              } catch(InvalidKeyException e) {
            	  System.out.println(e);
              } catch(IllegalBlockSizeException e) {
            	  System.out.println(e);
              } catch(BadPaddingException e) {
            	  System.out.println(e);
              }
              String messaggioDecifrato = new String(dectyptedText);
              JSONObject message = new JSONObject(messaggioDecifrato);
            
    		BigInteger idSessionUser=new BigInteger(""+message.getInt("sessionUser"));
    		BigInteger idSearchedUser=new BigInteger(""+message.getInt("searchedUser"));
    		BigInteger eval;
    		UserData requestor = new UserData(idSearchedUser);
    		UserData owner = new UserData(idSessionUser);
    		
    		eval = owner.getPolynomial(1).evaluate(idSearchedUser);
    		if (eval.equals(BigInteger.ZERO)) {
    			evaluation=1;
    			
    		}
    			
    		
    		eval = requestor.getPolynomial(1).evaluate(idSessionUser);
    		if (eval.equals(BigInteger.ZERO)) {
    			
    			evaluation=1;
    		}
    		
    		
			
			pw=res.getWriter();
			try{
	          	pw = res.getWriter();    	 	
	        	pw.println(evaluation);
	        	}catch(Exception ex)
	          	{
	          	pw.println("{");
	          	pw.println("\"successful\": false,");
	          	pw.println("\"message\": \""+ex.getMessage()+"\",");
	          	pw.println("}");
	          	return;
	          	}

    	}
       	
       	

        //------------------ Friendship creation--------------------------------------------------------
	
     	@RequestMapping(value="/friendshipCreation",method=RequestMethod.POST)
    	public void friendshipCreation(HttpServletResponse res,@RequestBody String messageToPFS)throws IOException, JSONException, NoSuchAlgorithmException, InvalidKeySpecException{
       	
     		RSAPrivateKeySpec spec = new RSAPrivateKeySpec(modulus,private_exponent);
        	KeyFactory factory = KeyFactory.getInstance("RSA");
        	PrivateKey privKey = factory.generatePrivate(spec);
        	
     		Cipher cipher;
     		byte[] dectyptedText = new byte[1];
     		
            try {
              cipher = javax.crypto.Cipher.getInstance("RSA");
              
              byte[] messaggioCifratoBytes = new byte[256];

              BigInteger messaggioCifrato = new BigInteger(messageToPFS, 16);
              if (messaggioCifrato.toByteArray().length > 256) {
                  for (int i=1; i<257; i++) {
                	  messaggioCifratoBytes[i-1] = messaggioCifrato.toByteArray()[i];
                  }
              } else {
            	  messaggioCifratoBytes = messaggioCifrato.toByteArray();
              }
             
              cipher.init(Cipher.DECRYPT_MODE, privKey);
              dectyptedText = cipher.doFinal(messaggioCifratoBytes);
              } catch(NoSuchAlgorithmException e) {
            	  System.out.println(e);
              } catch(NoSuchPaddingException e) { 
            	  System.out.println(e);
              } catch(InvalidKeyException e) {
            	  System.out.println(e);
              } catch(IllegalBlockSizeException e) {
            	  System.out.println(e);
              } catch(BadPaddingException e) {
            	  System.out.println(e);
              }
              String messaggioDecifrato = new String(dectyptedText);
              JSONObject message = new JSONObject(messaggioDecifrato);
            
              BigInteger idRequestor=new BigInteger(message.getString("idRequestor"));
              BigInteger idOwner=new BigInteger(message.getString("idOwner"));
              String emailRequestor= /*"dsn.project.p2p@gmail.com";*/message.getString("emailRequestor"); 
              String nameRequestor=message.getString("nameRequestor");
              String surnameRequestor=message.getString("surnameRequestor");
              String nameOwner=message.getString("nameSearched");
              String surnameOwner=message.getString("surnameSearched");
              
              LinkedList<BigInteger> friendshipPolynomial = new LinkedList<BigInteger>();
      			Polynomial tmpPoly;
      			LinkedList<Polynomial> firstPropagation,secondPropagation;
      		
      		try {
      			
      			friendshipPolynomial.add(new BigInteger("1"));
      			friendshipPolynomial.add(new BigInteger("1"));
      	
      			UserData first = new UserData(idRequestor);
      			UserData second = new UserData(idOwner);
      	
      			// Aggiungo il contatto 'second' a PL1_first
      			friendshipPolynomial.set(1, new BigInteger("-"+idOwner));
      			tmpPoly = new Polynomial(friendshipPolynomial);
      			tmpPoly = first.getPolynomial(1).threadedConvolution(tmpPoly, executor);
      			first.setPolynomial(1, tmpPoly);

      			// Aggiungo il contatto 'first' a PL1_second
      			friendshipPolynomial.set(1, new BigInteger("-"+idRequestor));
      			tmpPoly = new Polynomial(friendshipPolynomial);
      			tmpPoly = second.getPolynomial(1).threadedConvolution(tmpPoly, executor);
      			second.setPolynomial(1, tmpPoly);

      			// Calcolo delle propagazioni
      			firstPropagation = new LinkedList<Polynomial>();
      			secondPropagation = new LinkedList<Polynomial>();
      			for (int i=2; i<UserData.MAX_DEPTH; i++) {

      				firstPropagation.add( first.getPolynomial(i).threadedConvolution(second.getPolynomial(i-1), executor) );
      				secondPropagation.add( second.getPolynomial(i).threadedConvolution(first.getPolynomial(i-1), executor) );
      			}
      			
      			// Propagazioni
      			for (int i=2; i<UserData.MAX_DEPTH; i++) {
      				first.setPolynomial(i, firstPropagation.get(i-2));
      				second.setPolynomial(i, secondPropagation.get(i-2));
      			}
      			
      			
      		} catch (InterruptedException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		} catch (ExecutionException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}
      		
      		String txt_email="Hi "+ nameRequestor+" "+surnameRequestor+"!\n"+nameOwner+" "+surnameOwner +" has accepted to be your friend!";
    		System.out.print(emailRequestor);
    		SendEmail emailtoSend=new SendEmail(emailRequestor);
    		emailtoSend.setText(txt_email);
    		emailtoSend.setSubject("Request friendship");
    		emailtoSend.send();
    		
     	}
     		
       		
       	
    	     		
       	
      //-------------------Evaluation request download-------------------------------------------------------

    	//Download MODIFICARE
    	@RequestMapping(value="/evaluationRequest/",method=RequestMethod.POST)
    	public void evaluationRequest(HttpServletResponse res,HttpServletRequest req) throws IOException, JSONException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, DecoderException{
        	PrintWriter pw=null;
        	int found=0;
    		StringBuilder sb = new StringBuilder();
            BufferedReader br = req.getReader();
            String str = null;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        	
        	RSAPrivateKeySpec spec = new RSAPrivateKeySpec(modulus,private_exponent);
        	KeyFactory factory = KeyFactory.getInstance("RSA");
        	PrivateKey privKey = factory.generatePrivate(spec);
        	
            byte [] encryptedmsgfromRMS=Hex.decodeHex(sb.toString().toCharArray()); 
    		Cipher cipher = Cipher.getInstance("RSA");
    		cipher.init(Cipher.DECRYPT_MODE, privKey);
    		byte [] decryptedmsgfromRMS=blockCipher(encryptedmsgfromRMS,Cipher.DECRYPT_MODE, cipher);	
            
            JSONObject message=new JSONObject(new String(decryptedmsgfromRMS,"UTF-8").toString());
   
    		BigInteger idRequestor=new BigInteger(""+message.getInt("idRequestor"));
    		BigInteger idOwner=new BigInteger(""+message.getInt("idOwner"));
    		Integer rule=message.getInt("ruleRsc");
    		String msgtoKMS=message.getString("msgtoKMS");
    		if(idRequestor.equals(idOwner)){
    			found=1;
    		}else{
    		BigInteger eval;
    		UserData requestor = new UserData(idRequestor);
    		UserData owner = new UserData(idOwner);
    		
    		eval = owner.getPolynomial(rule).evaluate(idRequestor);
    		if (eval.equals(BigInteger.ZERO)) {
    			
    			found=1;
    		}
    			else{					//CONTROLLO AGGIUNTO DA ME: VERIFICO CHE CI SIANO POLINOMI DI LIVELLO INFERIORE PER I QUALI L'EVALUATE==0
    			int cont=rule;
    				while(cont!=1){
    					if (owner.getPolynomial(cont-1).evaluate(idRequestor).equals(BigInteger.ZERO))
    						found=1;
    					cont=cont-1;
    				}								
    		}
    		
    		eval = requestor.getPolynomial(rule).evaluate(idOwner);
    		if (eval.equals(BigInteger.ZERO)) {
    			
    			found=1;
    		}
    		else{					//CONTROLLO AGGIUNTO: VERIFICO CHE CI SIANO POLINOMI DI LIVELLO INFERIORE PER I QUALI L'EVALUATE==0
    			int cont=rule;
    				while(cont!=1){
    					if (owner.getPolynomial(cont-1).evaluate(idRequestor).equals(BigInteger.ZERO))
    						found=1;
    					cont=cont-1;
    				}								
    		}
    		}
    		
    		
    		pw=res.getWriter();
    		JSONObject jsonToKMS=new JSONObject();
    		jsonToKMS.put("msgtoKMS", msgtoKMS);	
    		jsonToKMS.put("found", found);
    		
    		RSAPublicKeySpec spec_public = new RSAPublicKeySpec(modulus_KMS,public_exponent_KMS);
        	factory = KeyFactory.getInstance("RSA");
        	PublicKey publicKey_KMS = factory.generatePublic(spec_public);
        	
        	
        	
        	
            byte [] encryptedmsgtoKMS=jsonToKMS.toString().getBytes("UTF-8");
    		cipher = Cipher.getInstance("RSA");
    		cipher.init(Cipher.ENCRYPT_MODE, publicKey_KMS);
    		encryptedmsgtoKMS=blockCipher(encryptedmsgtoKMS,Cipher.ENCRYPT_MODE, cipher);	
            
    		char[] encryptedTranspherable = Hex.encodeHex(encryptedmsgtoKMS);
   		   	String strmsgencrypted=new String(encryptedTranspherable);
        	
    		
    		URL url = new URL("http://193.206.170.148/KMS/evaluationInProcess/");						//INVIA IL MSG A PFS CON RICHIESTA DI EVALUATION
       		
   		   URLConnection urlConnection = url.openConnection();
   		   urlConnection.setDoOutput(true);
   		   urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
   		   urlConnection.connect();
   		   OutputStream outputStream = urlConnection.getOutputStream();
   		   outputStream.write(strmsgencrypted.getBytes());		
   		   outputStream.flush();
   		   
   		  BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		   

	       StringBuffer responseFromKMS = new StringBuffer(); 
	       String line;
	       while((line = reader.readLine()) != null) {
	    	 responseFromKMS.append(line);
	    	 responseFromKMS.append('\r');
	       }
	       
	       pw.println(responseFromKMS.toString());

    		
    	}
    	
    	private static byte[] blockCipher(byte[] bytes, int mode, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException{
    		
    		// string initialize 2 buffers.
    		// scrambled will hold intermediate results
    		byte[] scrambled = new byte[0];

    		// toReturn will hold the total result
    		byte[] toReturn = new byte[0];
    		// if we encrypt we use 100 byte long blocks. Decryption requires 128 byte long blocks (because of RSA)
    		int length = (mode == Cipher.ENCRYPT_MODE)? 100 : 256;

    		// another buffer. this one will hold the bytes that have to be modified in this step
    		byte[] buffer = new byte[length];

    		for (int i=0; i< bytes.length; i++){

    			// if we filled our buffer array we have our block ready for de- or encryption
    			if ((i > 0) && (i % length == 0)){
    				//execute the operation
    				scrambled = cipher.doFinal(buffer);
    				// add the result to our total result.
    				toReturn = append(toReturn,scrambled);
    				// here we calculate the length of the next buffer required
    				int newlength = length;

    				// if newlength would be longer than remaining bytes in the bytes array we shorten it.
    				if (i + length > bytes.length) {
    					 newlength = bytes.length - i;
    				}
    				// clean the buffer array
    				buffer = new byte[newlength];
    			}
    			// copy byte into our buffer.
    			buffer[i%length] = bytes[i];
    		}

    		// this step is needed if we had a trailing buffer. should only happen when encrypting.
    		// example: we encrypt 110 bytes. 100 bytes per run means we "forgot" the last 10 bytes. they are in the buffer array
    		scrambled = cipher.doFinal(buffer);

    		// final step before we can return the modified data.
    		toReturn = append(toReturn,scrambled);

    		return toReturn;
    	}
    	
    	private static byte[] append(byte[] prefix, byte[] suffix){
    		byte[] toReturn = new byte[prefix.length + suffix.length];
    		for (int i=0; i< prefix.length; i++){
    			toReturn[i] = prefix[i];
    		}
    		for (int i=0; i< suffix.length; i++){
    			toReturn[i+prefix.length] = suffix[i];
    		}
    		return toReturn;
    	}
    		
    		
} 	
