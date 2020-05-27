package rest;

@SuppressWarnings("serial")
public class UsuarioException extends Exception{
	
	public UsuarioException(String msg, Throwable causa) {
		super(msg, causa);
	}
	
	public UsuarioException(String msg) {
		super(msg);
	}
	

}
