package bookle.rest;

import java.net.URI;


import java.util.List;

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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import bookle.controlador.BookleControlador;
import bookle.controlador.BookleControladorImpl;
import bookle.tipos.Actividad;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@Path("actividades")
@Api
public class BookleRest {

	@Context private UriInfo uriInfo;

	private BookleControlador controlador = new BookleControladorImpl();

	@GET
	@Path("/{id}")
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	@ApiOperation(
			value="Consulta una actividad",
			notes = "Retorna una actividad utilizando su identificador",
			response=Actividad.class)
	@ApiResponses(
			value= {
				@ApiResponse(code=HttpServletResponse.SC_OK, message=""),
				@ApiResponse(code=HttpServletResponse.SC_NOT_FOUND, message="Actividad no encontrada")
			})	
	public Response getActividad(@ApiParam(value="id de la actividad", required = true) @PathParam("id") String id) throws BookleException {
		Actividad actividad = controlador.getActividad(id);
		return Response.status(Response.Status.OK).entity(actividad).build();
	}

	@PUT
	@Path("{id}")
	public Response updateActividad(
			@PathParam("id") String id, 
			@FormParam("titulo") String titulo,
			@FormParam("descripcion") String descripcion,
			@FormParam("profesor") String profesor,
			@FormParam("email") String email) throws BookleException {
		controlador.updateActividad(id, titulo, descripcion, profesor, email);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@DELETE
	@Path("{id}")
	public Response removeActividad(@PathParam("id") String id) throws BookleException {
		controlador.removeActividad(id);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@POST
	public Response createActividad(
			@FormParam("titulo") String titulo, 
			@FormParam("descripcion") String descripcion,
			@FormParam("profesor") String profesor, 
			@FormParam("email") String email) throws BookleException {
		String id = controlador.createActividad(titulo, descripcion, profesor, email);

		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(id);
		URI nuevaURL = builder.build();

		return Response.created(nuevaURL).build();
	}

	/*
	 * @DELETE
	 * 
	 * @Path("{id}/agenda/{fecha}") public Response removeDiaActividad(
	 * 
	 * @PathParam("id") String id,
	 * 
	 * @PathParam("fecha") Date fecha) throws BookleException{
	 * controlador.removeDiaActividad(id, fecha); return
	 * Response.status(Response.Status.NO_CONTENT).build(); }
	 * 
	 * @DELETE
	 * 
	 * @Path("{id}/agenda/{fecha}/turno/{indice}") public Response
	 * removeTurnoActividad(
	 * 
	 * @PathParam("id") String idActividad,
	 * 
	 * @PathParam("fecha") Date fecha,
	 * 
	 * @PathParam("indice") int turno) throws BookleException{
	 * controlador.removeTurnoActividad(idActividad, fecha, turno); return
	 * Response.status(Response.Status.NO_CONTENT).build(); }
	 * 
	 * @DELETE
	 * 
	 * @Path("/{id}/reservas/{ticket}") public Response removeReserva(
	 * 
	 * @PathParam("id") String idActividad,
	 * 
	 * @PathParam("ticket") String ticket) throws BookleException{
	 * 
	 * controlador.removeReserva(idActividad, ticket); return
	 * Response.status(Response.Status.NO_CONTENT).build();
	 * 
	 * }
	 * 
	 * @PUT
	 * 
	 * @Path("/{id}/agenda/{fecha}") public Response addDiaActividad(
	 * 
	 * @PathParam("id") String id,
	 * 
	 * @PathParam("fecha") Date fecha, int turnos) throws BookleException{
	 * controlador.addDiaActividad(id, fecha, turnos); return
	 * Response.status(Response.Status.NO_CONTENT).build(); }
	 * 
	 * @PUT
	 * 
	 * @Path("/{id}/agenda/{fecha}") public Response addTurnoActividad(
	 * 
	 * @PathParam("id") String id,
	 * 
	 * @PathParam("fecha") Date fecha) throws BookleException {
	 * controlador.addTurnoActividad(id, fecha); return
	 * Response.status(Response.Status.NO_CONTENT).build(); }
	 * 
	 * @PUT
	 * 
	 * @Path("/{id}/agenda/{fecha}/turno/{indice}") public Response setHorario (
	 * 
	 * @PathParam("id") String idActividad,
	 * 
	 * @PathParam("fecha")Date fecha,
	 * 
	 * @PathParam("turno")int indice, String horario)throws BookleException {
	 * controlador.setHorario(idActividad, fecha, indice, horario); return
	 * Response.status(Response.Status.NO_CONTENT).build(); }
	 * 
	 * @POST
	 * 
	 * @Path("/{id}/reservas") public Response createReserva(
	 * 
	 * @PathParam("id") String idActividad,
	 * 
	 * @FormParam("fecha") Date fecha,
	 * 
	 * @FormParam("indice") int indice,
	 * 
	 * @FormParam("alumno") String alumno,
	 * 
	 * @FormParam("email") String email) throws BookleException{ String id =
	 * controlador.createReserva(idActividad, fecha, indice, alumno, email);
	 * 
	 * UriBuilder builder = uriInfo.getAbsolutePathBuilder();
	 * builder.path("reservas"); builder.path(id); URI nuevaURL = builder.build();
	 * return Response.created(nuevaURL).build(); }
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getActividades() throws BookleException {
		List<Actividad> lista = controlador.getActividades();
		GenericEntity<List<Actividad>> entidades = new GenericEntity<List<Actividad>>(lista) {
		};
		return Response.status(Response.Status.OK).entity(entidades).build();
	}

}
