package Rest;

@SuppressWarnings("serial")
public class RecursoNoEncontradoException extends RuntimeException{ //Excepcion que ocurre cuando no se encuentra recurso
	
public RecursoNoEncontradoException(String msg) {
		
		super(msg);
	}

}
