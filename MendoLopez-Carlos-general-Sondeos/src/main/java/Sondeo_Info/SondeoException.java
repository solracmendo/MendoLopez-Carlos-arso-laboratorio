package Sondeo_Info;


@SuppressWarnings("serial")
public class SondeoException extends Exception{
	
public SondeoException(String msg, Throwable causa) {
		
		super(msg, causa);
	}
	
	public SondeoException(String msg) {
		
		super(msg);
	}

}
