package it.insubria.dista;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.math.BigInteger;


public class Evaluation {
	
	public static BufferedWriter output;
	
	public static boolean evaluationRule(BigInteger owner,Integer rule,BigInteger requestor){
		
try {
			
					System.out.println("Valutazione della regola "+rule+ " sul path "+owner+"->"+requestor);
						
						UserData Owner = new UserData(owner);
						long start = System.currentTimeMillis();
						BigInteger evaluation = Owner.getPolynomial(rule).evaluate(requestor);
						

						if (evaluation.equals(BigInteger.ZERO)) {
							
							return true;
						} else {
							return false;
						}
						
					}
				
				
				 catch (Exception e) {
			e.printStackTrace();
		}
return false;
	}
}

		
	


