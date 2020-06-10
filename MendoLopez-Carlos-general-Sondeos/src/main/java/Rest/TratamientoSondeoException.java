package Rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ExceptionMapper;

import Sondeo_Info.SondeoException;

@Provider
public class TratamientoSondeoException implements ExceptionMapper<SondeoException>{
	//Controlador global de excepciones
	@Override
	public Response toResponse(SondeoException arg0) {
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(arg0.getMessage()).build();
	}
	

}
