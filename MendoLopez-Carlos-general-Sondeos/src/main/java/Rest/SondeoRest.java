package Rest;

import java.net.URI;
import java.util.Collection;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
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
import io.swagger.jaxrs.PATCH;

@Path("sondeos")
public class SondeoRest {
	
	@Context 
	private UriInfo uriInfo;
	
	private Controlador_Impl controlador = Controlador_Impl.getControlador();
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSondeo(@PathParam("id") String id) throws SondeoException {
		Sondeo sondeo = controlador.getSondeo(id);
		return Response.status(Response.Status.OK).entity(sondeo).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllSondeos() throws SondeoException{
		Collection<Sondeo> sondeos = controlador.getAllSondeo();
		JsonArrayBuilder builder = Json.createArrayBuilder();
		
		for(Sondeo sondeo : sondeos) {
			UriBuilder constructor = uriInfo.getAbsolutePathBuilder();
			constructor.path(sondeo.getId());
			String url_sondeo = constructor.build().toString();
			
			JsonObject sondeoJson = Json.createObjectBuilder()
					.add("_links",
							Json.createObjectBuilder().add("self", Json.createObjectBuilder().add("href", url_sondeo)))
					.add("pregunta", sondeo.getPregunta()).build();
			
			builder.add(sondeoJson);
			
		}
		
		JsonObject jsonSondeos = Json.createObjectBuilder()
				.add("_links",
						Json.createObjectBuilder().add("self",
								Json.createObjectBuilder().add("href",
										uriInfo.getAbsolutePathBuilder().build().toString())))
				.add("total", sondeos.size()).add("_embedded", builder.build()).build();
				
				
		
		return Response.ok(jsonSondeos.toString()).build();
	}
	
	@PATCH
	@Path("/{id}")
	public Response updateSondeo(@PathParam("id") String id,
			@FormParam("respuesta") String respuesta,
			@FormParam("email") String email) throws SondeoException {
		if(controlador.anadirRespuesta(id, respuesta,email)) {
		
		return Response.status(Response.Status.NO_CONTENT).build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	@PUT
	@Path("/{id}")
	public Response contestarPregunta(@PathParam("id") String id,
			@FormParam("resolucion") String resolucion,
			@FormParam("email") String email) throws SondeoException {
		if(controlador.contestarPregunta(id, resolucion, email)) {
		
		return Response.status(Response.Status.NO_CONTENT).build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	@DELETE
	@Path("/{id}")
	public Response removeSondeo(@PathParam("id") String id,
			@FormParam("email") String email) throws SondeoException {
		if(controlador.removeSondeo(id,email) == true) {
			return Response.status(Response.Status.NO_CONTENT).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}	
	}
	
	@POST
	public Response createActividad(
			@FormParam("pregunta") String pregunta,
			@FormParam("descripcion")String descripcion,
			@FormParam("inicio") String inicio,
			@FormParam("fin") String fin,
			@FormParam("minimo") String minimo,
			@FormParam("maximo") String maximo,
			@FormParam("email") String email) throws SondeoException {
		
		String id = controlador.createSondeo(pregunta, descripcion, inicio, fin, minimo, maximo,email);
		
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(id);
		URI nuevaURL = builder.build();
		
		return Response.created(nuevaURL).build();
	}
	
	

}
