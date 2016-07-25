package it.insubria.dista;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

import it.insubria.dista.Polynomials.Polynomial;


public class Build {
	
	public static BufferedWriter output;
	
	public static void buildGraph(){
		PathFinder PFS = null;
		try {
	
		output = new BufferedWriter(new FileWriter(new File(".result")),32768);
		
		Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PFS","root","");
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
				connection.close();
				
				PFS = PathFinder.getInstance();
	
				LinkedList<BigInteger> coeff = new LinkedList<BigInteger>();
				coeff.add(new BigInteger("1"));
				coeff.add(new BigInteger("1"));

				BufferedReader br = new BufferedReader(new FileReader("/Users/Ery/Documents/adjacency.txt"));
				String line;
				long startExecTest = System.currentTimeMillis();

				while ((line = br.readLine()) != null) {
					
					if (line.indexOf('#') > -1) {
						String[] data = line.split("@");
	
						BigInteger userId = new BigInteger(data[0]);
						Polynomial directs = new Polynomial(new BigInteger("1"));
						

						String[] contactIds = data[1].split("#");
						for (String contactId : contactIds) {
							coeff.set(1, new BigInteger("-"+contactId));
							Polynomial poly = new Polynomial(coeff);
							directs.threadedConvolution(poly, PFS.getExecutor());
						}

						PFS.addUserData(new UserData(userId, directs), true);

					}

				}

				System.out.println("\n\nTempo totale per la lettura del dataset: "+(System.currentTimeMillis()-startExecTest)+"ms");
			output.write("\n\nTempo totale per la lettura del dataset: "+(System.currentTimeMillis()-startExecTest)+"ms");
				output.flush();
				output.close();
			} else {
				
				PFS = PFS.restore();
			}

			
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	}


