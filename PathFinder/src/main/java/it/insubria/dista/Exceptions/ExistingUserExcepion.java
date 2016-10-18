package it.insubria.dista.Exceptions;

public class ExistingUserExcepion extends Exception{
	
	private String msg;
	
	public ExistingUserExcepion(String s){
		super(s);
		this.msg=s;
	}

	@Override
	public String toString() {
		return "ExistingUserExcepion [msg=" + msg + "]";
	}

	
}
