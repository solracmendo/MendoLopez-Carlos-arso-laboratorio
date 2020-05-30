package controlador;

import java.util.ArrayList;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import mongo.UsuarioRepository;
import rest.RecursoNoEncontradoException;
import rest.UsuarioException;
import usuario_Info.Usuario;

public class Controlador_Impl implements Controlador{
	
	private static Controlador_Impl controlador;
	private static UsuarioRepository usuarioRepository = null;
	private static MongoClient client = null;
	
	private static MongoClientURI uri = null;
	
	public static Controlador_Impl getControlador() {
		if(controlador == null) {
			controlador = new Controlador_Impl();
			initDB();
		}
		return controlador;
	}

	@Override
	public String createUsuario(String nombre, String email, String rol) throws UsuarioException{
		if(nombre == null || nombre.equals("")) {
			throw new IllegalArgumentException("El nombre debe ser no nulo y contener informacion");
		}
		
		if(email == null || email.equals("")) {
			throw new IllegalArgumentException("El email debe ser no nulo y contener informacion");
		}
		
		if(rol == null || rol.equals("")) {
			throw new IllegalArgumentException("El rol debe ser no nulo y contener informacion");
		}
		
		if(client == null) {
			iniciarCliente();
		}
		try {
			Usuario antiguoUsuario = new Usuario(nombre,email,rol);
			Usuario nuevoUsuario = usuarioRepository.saveUsuario(antiguoUsuario);
			return nuevoUsuario.getId();
		} catch(Exception e) {
			throw new UsuarioException("Se ha producido un error durante el acceso a la base de datos");
		} finally {
			cerrarCliente();
		}
	}

	@Override
	public ArrayList<Usuario> getAllProfesores() throws UsuarioException{
		if(client == null) {
			iniciarCliente();
		}
		try {
			ArrayList<Usuario> profesores = usuarioRepository.getAllProfesores();
			return profesores;
		} catch (Exception e) {
			throw new UsuarioException("Se ha producido un error durante el acceso a la base de datos");
		} finally {
			cerrarCliente();
		}
	}

	@Override
	public ArrayList<Usuario> getAllEstudiantes() throws UsuarioException{
		if(client == null) {
			iniciarCliente();
		}
		try {
			ArrayList<Usuario> estudiantes = usuarioRepository.getAllEstudiantes();
			return estudiantes;
		} catch (Exception e) {
			throw new UsuarioException("Se ha producido un error durante el acceso a la base de datos");
		} finally {
			cerrarCliente();
		}
	}

	@Override
	public boolean isAlumno(String email) throws UsuarioException{
		if(email == null || email.equals("")) {
			throw new IllegalArgumentException("El email debe ser no nulo y contener informacion");
		}
		
		if(!existUsuario(email)) { //Vigilar esto por si esta bien PUESTO
			throw new RecursoNoEncontradoException("El recurso no se encuentra en la base de datos");
		}
		
		if(client == null) {
			iniciarCliente();
		}
		
		
		try {
			boolean resultado = usuarioRepository.isAlumno(email);
			return resultado;
		} catch (Exception e) {
			throw new UsuarioException("Se ha producido un error durante el acceso a la base de datos");
		} finally {
			cerrarCliente();
		}	
	}
	
	@Override
	public boolean existUsuario (String email) throws UsuarioException{
		if(email == null || email.equals("")) {
			throw new IllegalArgumentException("El email debe ser no nulo y contener informacion");
		}
		
		if(client == null) {
			iniciarCliente();
		}
		try {
			boolean resultado = usuarioRepository.existUsuario(email);
			return resultado;
		} catch (Exception e) {
			throw new UsuarioException("Se ha producido un error durante el acceso a la base de datos");
		} finally {
			cerrarCliente();
		}
	}
	
	private static void initDB() {
		uri = new MongoClientURI("mongodb://arso:arso-20@cluster0-shard-00-00-slp29.azure.mongodb.net:27017,cluster0-shard-00-01-slp29.azure.mongodb.net:27017,cluster0-shard-00-02-slp29.azure.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority");
		client = new MongoClient(uri);
		MongoDatabase mong = client.getDatabase("Users");
		usuarioRepository = new UsuarioRepository(mong.getCollection("usuarios"));
	}
	
	private static void iniciarCliente() {
		client = new MongoClient(uri);
		MongoDatabase mong = client.getDatabase("Users");
		usuarioRepository = new UsuarioRepository(mong.getCollection("usuarios"));
		
	}
	
	private static void cerrarCliente() {
		client.close();
		client = null;
		usuarioRepository = null;
	}
	
	
	

}
