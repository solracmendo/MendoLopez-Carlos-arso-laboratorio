package rest;

import java.net.URI;
import java.util.Collection;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import controlador.Controlador_Impl;
import usuario_Info.Usuario;

@Path("usuarios")
public class UsuarioRest {
	
	@Context
	private UriInfo uriInfo;
	
	private Controlador_Impl controlador = Controlador_Impl.getControlador();
	
	@POST
	public Response createUsuario(
			@FormParam("nombre") String nombre,
			@FormParam("email") String email,
			@FormParam("rol") String rol) throws UsuarioException { //Operacion de creacion de usuarios
		
		String id = controlador.createUsuario(nombre, email, rol);
		
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		
		builder.path(id);
		
		URI nuevaURL = builder.build();
		
		return Response.created(nuevaURL).build();
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsersRol(
			@QueryParam("rol") String rol) throws UsuarioException{ //Obtener lista de usuarios por rol
		
		Collection<Usuario> usuariosSeleccionados = null;
		
		if(rol.equals("profesor")){
			usuariosSeleccionados = controlador.getAllProfesores();
		} else if(rol.equals("estudiante")){ //Elegimos estudiantes
			usuariosSeleccionados = controlador.getAllEstudiantes();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		return Response.ok(usuariosSeleccionados).build();
	}
	
	@GET
	@Path("/{email}")
	public Response isAlumno(
			@PathParam("email")String email) throws UsuarioException{ //Comprobar si un email pertenece a un alumno
		boolean existeAlumno = controlador.existUsuario(email);
		if(existeAlumno) {
		
		boolean isAlumno = controlador.isAlumno(email);
		
		if(isAlumno) {
			return Response.status(Response.Status.OK).build();
		} else {
			return Response.status(Response.Status.NOT_ACCEPTABLE).build();
		}
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

}
