package bookle.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;


public class TratamientoBookleException implements ExceptionMapper<BookleException>{
	@Override
	public Response toResponse(BookleException arg0) {
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(arg0.getMessage()).build();
	}
}
