package controlador;

import java.util.ArrayList;

import rest.UsuarioException;
import usuario_Info.Usuario;

public interface Controlador {
	
	String createUsuario (String nombre, String email, String rol) throws UsuarioException;
	
	ArrayList<Usuario> getAllProfesores() throws UsuarioException;
	
	ArrayList<Usuario> getAllEstudiantes() throws UsuarioException;
	
	boolean isAlumno(String email) throws UsuarioException;

	boolean existUsuario(String email) throws UsuarioException;

}
