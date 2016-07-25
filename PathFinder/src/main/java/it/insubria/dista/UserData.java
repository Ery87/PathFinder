package it.insubria.dista;

import it.insubria.dista.Polynomials.Polynomial;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class UserData {

	public final static int MAX_DEPTH = 7;

	private BigInteger userId;
	
	public UserData(BigInteger userId) {
		this.userId = userId;
	}
	
	public UserData(BigInteger userId, Polynomial directContacts) {
		
		boolean stored = false;
		while (!stored) {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PFS","root","");
				Statement state = connection.createStatement();
				
				String insertion = "INSERT INTO polynomials SET uid="+userId;
				insertion += ", polyLv1='"+(new Polynomial(directContacts).toString())+"'";
				for (int i=2; i<=MAX_DEPTH; i++) {
					insertion += ", polyLv"+i+"='"+(new Polynomial(new BigInteger("1"))).toString()+"'";
				}
				insertion += ";";
	
				state.executeUpdate(insertion);
				
				this.userId = userId;
				
				connection.close();
				stored = true;
			} catch (Exception e) {
				try {
					System.out.println("Occurred Exception "+e.getClass());
					Build.output.write("Occurred Exception "+e.getClass()+"\n");
					Thread.sleep(500);
				} catch (Exception sleep) {
				}
			}
		}
	}
	
	public BigInteger getUserId() {
		return this.userId;
	}
	
	public Polynomial getPolynomial(int level) {
		while (true) {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PFS","root","");
				Statement state = connection.createStatement();

				String select = "SELECT polyLv"+level+" FROM polynomials WHERE uid="+this.userId;
				
				ResultSet rs = state.executeQuery(select);
				
				String poly = "";
				while(rs.next()) {
					poly = (String)rs.getObject("polyLv"+level);

				}
				


				connection.close();
				
				return new Polynomial(poly);
			} catch (Exception e) {
				try {
					System.out.println("Occurred Exception "+e.getClass());
					Build.output.write("Occurred Exception "+e.getClass()+"\n");
					Thread.sleep(500);
				} catch (Exception sleep) {
				}
			}
		}
	}
	
	public void setPolynomial(int level, Polynomial polynomial) {
		while (true) {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PFS","root","");
				Statement state = connection.createStatement();
	
				String update = "UPDATE polynomials SET polyLv"+level+"='"+polynomial.toString()+"'";
				update += " WHERE uid="+this.userId;
		
				state.executeUpdate(update);
				
				connection.close();
				return;
			} catch (Exception e) {
				try {
					System.out.println("Occurred Exception "+e.getClass());
					Build.output.write("Occurred Exception "+e.getClass()+"\n");
					Thread.sleep(500);
				} catch (Exception sleep) {
				}
			}
		}
	}
	
	public boolean exists() {
		while (true) {
			try {
				
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PFS","root","");
				Statement state = connection.createStatement();

				ResultSet rs = state.executeQuery("SELECT COUNT(uid) FROM polynomials WHERE uid="+this.userId);
				
				while (rs.next()) {
					int result = rs.getInt("count(uid)");
					if (result == 0) 
						return false;
					else
						return true;
				}
				
			} catch (Exception e) {
				try {
					System.out.println("Occurred Exception "+e.getClass());
					Build.output.write("Occurred Exception "+e.getClass()+"\n");
					Thread.sleep(500);
				} catch (Exception sleep) {
				}
			}
		} 
	}
}
