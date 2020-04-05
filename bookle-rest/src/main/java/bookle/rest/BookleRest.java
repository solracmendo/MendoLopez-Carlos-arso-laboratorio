package bookle.rest;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import bookle.controlador.BookleControlador;
import bookle.controlador.BookleControladorImpl;
import bookle.controlador.ItemLista;
import bookle.controlador.ListadoActividades;
import bookle.tipos.Actividad;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@Path("actividades")
@Api
public class BookleRest {

	@Context
	private UriInfo uriInfo;

	private BookleControlador controlador = new BookleControladorImpl();

	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Consulta una actividad", notes = "Retorna una actividad utilizando su identificador", response = Actividad.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Actividad no encontrada") })
	public Response getActividad(@ApiParam(value = "id de la actividad", required = true) @PathParam("id") String id)
			throws BookleException {
		Actividad actividad = controlador.getActividad(id);
		return Response.status(Response.Status.OK).entity(actividad).build();
	}

	@PUT
	@Path("/{id}")
	// Sin consumes
	@ApiOperation(value = "Actualiza una actividad", notes = "Actualiza una actividad usando su identificador y los datos proporcionados")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Actividad no encontrada"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "El formato de la peticion no es correcto") })
	public Response updateActividad(@ApiParam(value = "id de la actividad", required = true) @PathParam("id") String id,
			@ApiParam(value = "titulo de la actividad", required = true) @FormParam("titulo") String titulo,
			@ApiParam(value = "descripcion de la actividad", required = false) @FormParam("descripcion") String descripcion,
			@ApiParam(value = "profesor de la actividad", required = true) @FormParam("profesor") String profesor,
			@ApiParam(value = "email asociado a la actividad", required = false) @FormParam("email") String email)
			throws BookleException {
		controlador.updateActividad(id, titulo, descripcion, profesor, email);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@DELETE
	@Path("{id}")
	@ApiOperation(value = "Elimina una actividad", notes = "Elimina una actividad utilizando su identificador")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Actividad no encontrada"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "El formato de la peticion no es correcto") })
	public Response removeActividad(@ApiParam(value = "id de la actividad", required = true) @PathParam("id") String id)
			throws BookleException {
		controlador.removeActividad(id);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@POST
	@ApiOperation(value = "Crea una actividad", notes = "Crea una actividad utilizando los parametros asociados", response = URI.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_CREATED, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "El formato de la peticion no es correcto") })
	public Response createActividad(
			@ApiParam(value = "titulo de la actividad", required = true) @FormParam("titulo") String titulo,
			@ApiParam(value = "descripcion de la actividad", required = false) @FormParam("descripcion") String descripcion,
			@ApiParam(value = "profesor asociado a la actividad", required = true) @FormParam("profesor") String profesor,
			@ApiParam(value = "email asociado a la actividad", required = false) @FormParam("email") String email)
			throws BookleException {
		String id = controlador.createActividad(titulo, descripcion, profesor, email);

		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(id);
		URI nuevaURL = builder.build();

		return Response.created(nuevaURL).build();
	}

	@POST
	@Path("/{id}/agenda")
	@ApiOperation(
			value="Anadir un dia a una agenda",
			notes="Crea un dia en una agenda usando los parametros",
			response = URI.class)
	@ApiResponses(
			value = {
					@ApiResponse(code = HttpServletResponse.SC_CREATED,message=""),
					@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message="El formato de la peticion es incorrecto")
					
			})
	public Response addDiaActividad(
			@ApiParam(value="id de la actividad", required = true) @PathParam("id") String id,
			@ApiParam(value="fecha del dia a anadir", required=true) @FormParam("fecha") String fecha) throws BookleException, ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date date = format.parse(fecha);
		controlador.addDiaActividad(id, date,1);
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(fecha);
	    URI nuevaURL = builder.build();	    
	    return Response.created(nuevaURL).build();
	
	}

	@DELETE
	@Path("{id}/agenda/{fecha}")
	@ApiOperation(
			value="Eliminar un dia a la agenda",
			notes="Permite eliminar un dia a una agenda determinada")
	@ApiResponses(
			value = {
					@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message=""),
					@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "El formato de la peticion es incorrecto")
			})
	public Response removeDiaActividad(
			@ApiParam(value="id de la actividad", required=true) @PathParam("id") String id, 
			@ApiParam(value="fecha de la actividad", required=true) @PathParam("fecha") Date fecha)
			throws BookleException {
		controlador.removeDiaActividad(id, fecha);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@DELETE
	@Path("{id}/agenda/{fecha}/turno/{indice}")
	@ApiOperation(
			value="Eliminar un turno",
			notes="Permite eliminar un turno a un dia de agenda determinado")
	@ApiResponses(
			value = {
					@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message=""),
					@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "El formato de la peticion es incorrecto")
			})
	public Response removeTurnoActividad(

			@ApiParam(value="id de la actividad", required=true) @PathParam("id") String idActividad,

			@ApiParam(value="fecha de la actividad", required=true) @PathParam("fecha") Date fecha,

			@ApiParam(value="indice del turno", required=true) @PathParam("indice") int turno) throws BookleException {
		controlador.removeTurnoActividad(idActividad, fecha, turno);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@DELETE
	@Path("/{id}/reservas/{ticket}")
	@ApiOperation(
			value="Eliminar una reserva",
			notes="Permite eliminar una reserva de una actividad")
	@ApiResponses(
			value = {
					@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message=""),
					@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "El formato de la peticion es incorrecto")
			})
	public Response removeReserva(

			@ApiParam(value="id de la actividad", required=true) @PathParam("id") String idActividad,

			@ApiParam(value="ticket de la reserva", required=true) @PathParam("ticket") String ticket) throws BookleException {

		controlador.removeReserva(idActividad, ticket);
		return Response.status(Response.Status.NO_CONTENT).build();

	}

	@POST
	@Path("/{id}/agenda/{fecha}/turno")
	@ApiOperation(
			value="Anadir un turno a un dia",
			notes="Crea un turno en un dia de una agenda",
			response = URI.class)
	@ApiResponses(
			value = {
					@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message=""),
					@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "El formato de la peticion es incorrecto")
			})
	public Response addTurnoActividad(
			@ApiParam(value="id de la actividad", required=true) @PathParam("id") String id, 
			@ApiParam(value="fecha de la actividad", required=true) @PathParam("fecha") String fecha)
			throws BookleException, ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date date = format.parse(fecha);
		int turno = controlador.addTurnoActividad(id, date);
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(Integer.toString(turno));
		URI nuevaURL = builder.build();
		return Response.created(nuevaURL).build();
	}

	@PUT
	@Path("/{id}/agenda/{fecha}/turno/{indice}")
	@ApiOperation(
			value="Establecer el horario de un turno",
			notes="Modificar el horario de un turno determinado"
			)
	@ApiResponses(
			value = {
					@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message=""),
					@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "El formato de la peticion es incorrecto")
			})
	public Response setHorario(
			@ApiParam(value="id de la actividad", required=true) @PathParam("id") String idActividad, 
			@ApiParam(value="fecha del dia", required=true) @PathParam("fecha") String fecha,
			@ApiParam(value="indice del turno", required=true) @PathParam("indice") int indice, 
			@ApiParam(value="horario del turno", required=true) @FormParam("horario") String horario)
			throws BookleException, ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date date = format.parse(fecha);
		controlador.setHorario(idActividad, date, indice, horario);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	@POST
	@Path("/{id}/agenda/{fecha}/turno/{indice}/reserva")
	@ApiOperation(
			value="Establecer una reserva",
			notes="Determinar la reserva para un turno determinado"
			)
	@ApiResponses(
			value = {
					@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message=""),
					@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "El formato de la peticion es incorrecto")
			})
	public Response createReserva(
			@ApiParam(value="id de la actividad", required=true) @PathParam("id") String idActividad, 
			@ApiParam(value="fecha del dia", required=true) @PathParam("fecha") String fecha,
			@ApiParam(value="indice del turno", required=true) @PathParam("indice") int indice, 
			@ApiParam(value="nombre del alumno de la reserva", required=true) @FormParam("alumno") String alumno, 
			@ApiParam(value="email de la reserva", required=false) @FormParam("email") String email)
			throws BookleException, ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date date = format.parse(fecha);
		String id = controlador.createReserva(idActividad, date, indice, alumno, email);

		UriBuilder builder = uriInfo.getBaseUriBuilder();
		builder.path("actividades");
		builder.path(idActividad);
		builder.path("reservas");
		builder.path(id);
		URI nuevaURL = builder.build();
		return Response.created(nuevaURL).build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Retorna un listado de actividades", response = ListadoActividades.class)
	public Response listado(
			@ApiParam(value = "profesor de la actividad", required = false) @QueryParam("profesor") String profesor,
			@ApiParam(value = "titulo de la actividad", required = false) @QueryParam("titulo") String titulo)
			throws BookleException {

		Collection<String> actividades = controlador.getIdentifidores();
		ListadoActividades listado = new ListadoActividades();
		boolean filtrado = true;
		for (String id : actividades) {
			filtrado = true;
			Actividad act = controlador.getActividad(id);

			if (profesor != null && act.getProfesor() != profesor) {
				filtrado = false;
			}
			if (titulo != null && act.getTitulo().contains(titulo) == false) {
				filtrado = false;
			}

			String title = controlador.getActividad(id).getTitulo();
			// Cálculo de la URL
			UriBuilder builder = uriInfo.getAbsolutePathBuilder();
			builder.path(id);
			String url = builder.build().toString();

			ItemLista a = new ItemLista(url, title);
			if ((profesor != null && titulo != null) || filtrado == true) {
				listado.getActividad().add(a);
			}

		}

		return Response.ok(listado).build();
	}

}
