package Rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class TratamientoUsuarioDesautorizadoException implements ExceptionMapper<UsuarioDesautorizadoException>{
	@Override
	public Response toResponse(UsuarioDesautorizadoException arg0) {
		return Response.status(Response.Status.UNAUTHORIZED).entity(arg0.getMessage()).build();
	}
	

}
