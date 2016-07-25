package it.insubria.dista;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import it.insubria.dista.Polynomials.Polynomial;

public class ControllerPath {
	static BufferedReader br;
	
	public static String execution(String r) {
		PathFinder PFS = null;
		String result="";
		try{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PFS","root","");
			Statement state = connection.createStatement();
			PFS = PathFinder.getInstance();
			
			LinkedList<BigInteger> coeff = new LinkedList<BigInteger>();
			coeff.add(new BigInteger("1"));
			coeff.add(new BigInteger("1"));

		InputStream in=new ByteArrayInputStream(r.getBytes());
		br=new BufferedReader(new InputStreamReader(in));
		String line;
		
		while ((line = br.readLine()) != null) {
			line = line.replace(" ", "");

			if (line.matches(".*eval.*")) {
				
				String[] command = (line.split(":")[1]).split(",");
				if (command.length != 3) {
					return result="Comando errato";
					
				} else {
					System.out.println("Valutazione della regola "+command[2]+ " sul path "+command[1]+"->"+command[0]);
					
					UserData owner = new UserData(new BigInteger(command[1]));
					long start = System.currentTimeMillis();
					BigInteger evaluation = owner.getPolynomial(Integer.parseInt(command[2])).evaluate(new BigInteger(command[0]));
					if (evaluation.equals(BigInteger.ZERO)) {
						return result="Valutazione positiva. ";
					} else {
						return result="Valutazione negativa. ";
					}
					
				/*System.out.println("Tempo richiesto: "+(System.currentTimeMillis()-start)+"ms");*/
				}
			} else if (line.matches(".*ins.*")) {
				String[] data = (line.split(":")[1]).split("@");
				if (!(new UserData(new BigInteger(data[0]))).exists()) {
					
					BigInteger userId = new BigInteger(data[0]);
					Polynomial directs = new Polynomial(new BigInteger("1"));
					

					String[] contactIds = data[1].split("#");
					for (String contactId : contactIds) {
						coeff.set(1, new BigInteger("-"+contactId));
						Polynomial poly = new Polynomial(coeff);
						directs.threadedConvolution(poly, PFS.getExecutor());
					}

					PFS.addUserData(new UserData(userId, directs), true);
					return result="inserimento effettuato";

					
				} else {
					return result="Utente già presente nel database";
				}
			} else if (line.matches(".*exit.*")) {
				System.out.println("Program exit");
				System.exit(0);
			} 
			
			
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
		
	}
}
