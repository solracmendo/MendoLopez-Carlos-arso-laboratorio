package Rest;

@SuppressWarnings("serial")
public class UsuarioDesautorizadoException extends RuntimeException{ //Excepcion de usuario no autorizado
	
public UsuarioDesautorizadoException(String msg) {
		
		super(msg);
	}

}
