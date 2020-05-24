package Rest;

import java.net.URI;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import Sondeo_Info.Sondeo;
import Sondeo_Info.SondeoException;
import controlador.Controlador;
import controlador.Controlador_Impl;

@Path("sondeos")
public class SondeoRest {
	
	@Context 
	private UriInfo uriInfo;
	
	private Controlador controlador = new Controlador_Impl();
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSondeo(@PathParam("id") String id) throws SondeoException {
		Sondeo sondeo = controlador.getSondeo(id);
		return Response.status(Response.Status.OK).entity(sondeo).build();
	}
	
	@PUT
	@Path("/{id}")
	public Response updateSondeo(@PathParam("id") String id,
			@FormParam("respuesta") String respuesta) throws SondeoException {
		controlador.anadirRespuesta(id, respuesta);
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response removeSondeo(@PathParam("id") String id) throws SondeoException {
		controlador.removeSondeo(id);
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@POST
	public Response createActividad(
			@FormParam("pregunta") String pregunta,
			@FormParam("descripcion")String descripcion,
			@FormParam("inicio") String inicio,
			@FormParam("fin") String fin,
			@FormParam("minimo") Integer minimo,
			@FormParam("maximo") Integer maximo) throws SondeoException {
		
		String id = controlador.createSondeo(pregunta, descripcion, inicio, fin, minimo, maximo);
		
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(id);
		URI nuevaURL = builder.build();
		
		return Response.created(nuevaURL).build();
	}
	
	

}
