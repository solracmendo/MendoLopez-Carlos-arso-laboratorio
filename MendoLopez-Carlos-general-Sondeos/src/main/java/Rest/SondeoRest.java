package Rest;

import java.net.URI;
import java.util.Collection;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletResponse;
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
import controlador.Controlador_Impl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import io.swagger.jaxrs.PATCH;

@Path("sondeos")
@Api
public class SondeoRest {
	
	@Context 
	private UriInfo uriInfo;
	
	private Controlador_Impl controlador = Controlador_Impl.getControlador();
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Obtiene la informacion de un sondeo",
			notes = "Retoma informacion de un sondeo",
			response = Sondeo.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, message ="Se ha obtenido la informacion del mensaj"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Se ha producido un error interno en el servidor"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "No se ha encontrado el sondeo"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "La peticion no se ha formulado de manera correcta")
	})
	public Response getSondeo(
			@ApiParam(value = "id de la actividad", required = true) @PathParam("id") String id) throws SondeoException { //Obtener informacion de un sondeo
		Sondeo sondeo = controlador.getSondeo(id);
		return Response.status(Response.Status.OK).entity(sondeo).build();
	}
	
	@GET
	@ApiOperation(value = "Obtiene un listado de sondeos",
	notes = "Retoma las URL de los sondeos",
	response = String.class)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, message ="Se ha obtenido el listado"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Se ha producido un error interno en el servidor"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "No se ha encontrado el sondeo"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "La peticion no se ha formulado de manera correcta")
	})
	public Response getAllSondeos() throws SondeoException{ //Obtenere listdo de todos los sondeos
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
	@ApiOperation(value = "Anade una respuesta al sondeo",
	notes = "Permite anadir una respuesta a un sondeo determinado",
	response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Se ha anadido una respuesta al sondeo"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_MODIFIED, message = "No se ha modificado el sondeo"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message ="Al usuario no se le permite realizar la operacion"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Se ha producido un error interno en el servidor"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "No se ha encontrado el sondeo"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "La peticion no se ha formulado de manera correcta")
	})
	public Response updateSondeo(
			@ApiParam(value = "Id del sondeo", required = true) @PathParam("id") String id,
			@ApiParam(value = "Respuesta que se anade", required = true) @FormParam("respuesta") String respuesta,
			@ApiParam(value = "Email del usuario que realiza la accion", required = true) @FormParam("email") String email) throws SondeoException { //Anadir respuesta a un sondeo
		if(controlador.anadirRespuesta(id, respuesta,email)) {
		
		return Response.status(Response.Status.NO_CONTENT).build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(value = "Contesta una pregunta de un sondeo",
	notes = "Permite contestar a una pregunta de un sondeo",
	response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Se ha contestado al sondeo"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_MODIFIED, message = "No se ha modificado el sondeo"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message ="Al usuario no se le permite realizar la operacion"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Se ha producido un error interno en el servidor"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "No se ha encontrado el sondeo"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "La peticion no se ha formulado de manera correcta")
	})
	public Response contestarPregunta(
			@ApiParam(value="Id del sondeo", required = true)@PathParam("id") String id,
			@ApiParam(value = "Respuesta dentro de un sondeo", required = true)@FormParam("resolucion") String resolucion,
			@ApiParam(value = "Email del usuario que realiza la accion", required = true)@FormParam("email") String email) throws SondeoException { //Contestar sondeo
		if(controlador.contestarPregunta(id, resolucion, email)) {
		
		return Response.status(Response.Status.NO_CONTENT).build();
		} else {
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Elimina un sodeo",
	notes = "Permite eliminar un sondeo",
	response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Se ha eliminado el sondeo"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message ="El usuario no está autorizado para realizar la accion"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Se ha producido un error interno en el servidor"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "No se ha encontrado el sondeo"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "La peticion se ha formulado de manera incorrecta")
	})
	public Response removeSondeo(
			@ApiParam(value = "Id del sondeo", required = true)@PathParam("id") String id,
			@ApiParam(value = "Email del usuario que realiza la accion", required = true) @FormParam("email") String email) throws SondeoException { //Eliminar sondeo de la base de datos
		if(controlador.removeSondeo(id,email) == true) {
			return Response.status(Response.Status.NO_CONTENT).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}	
	}
	
	@POST
	@ApiOperation(value = "Crea un sondeo",
	notes = "Permite crear un sondeo",
	response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_CREATED, message = "Se ha creado el sondeo"),
			@ApiResponse(code = HttpServletResponse.SC_UNAUTHORIZED, message ="El usuario no esta autorizado para realizar esta accion"),
			@ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Se ha producido un error interno en el servidor"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "La peticion se ha formulado de manera correcta"),
			
	})
	public Response createActividad( //Crear actividad
			@ApiParam(value = "Pregunta del sondeo", required = true)@FormParam("pregunta") String pregunta,
			@ApiParam(value = "Descripcion del sondeo", required = true)@FormParam("descripcion")String descripcion,
			@ApiParam(value = "Comienzo del sondeo", required = true)@FormParam("inicio") String inicio,
			@ApiParam(value = "Fin del sondeo", required = true)@FormParam("fin") String fin,
			@ApiParam(value = "Minimo de respuestas permitidas", required = true)@FormParam("minimo") String minimo,
			@ApiParam(value = "Maximo de respuestas permitidas", required = true)@FormParam("maximo") String maximo,
			@ApiParam(value = "Email del usuario que realiza la accion", required = true)@FormParam("email") String email) throws SondeoException {
		
		String id = controlador.createSondeo(pregunta, descripcion, inicio, fin, minimo, maximo,email);
		
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(id);
		URI nuevaURL = builder.build();
		
		return Response.created(nuevaURL).build();
	}
	
	

}
