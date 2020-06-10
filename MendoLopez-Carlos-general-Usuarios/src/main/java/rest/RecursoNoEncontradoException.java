package rest;

@SuppressWarnings("serial")
public class RecursoNoEncontradoException extends RuntimeException {
	//Excepcion que ocurre cuando no encontramos un recurso
	
	public RecursoNoEncontradoException(String msg) {
        super(msg);
    }

}
