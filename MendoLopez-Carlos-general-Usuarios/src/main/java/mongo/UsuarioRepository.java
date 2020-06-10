package mongo;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import usuario_Info.Usuario;

public class UsuarioRepository {
	
	private final MongoCollection<Document> usuarios;
	
	public UsuarioRepository(MongoCollection<Document> usuarios) {
		this.usuarios = usuarios;
	}
	
	private Usuario usuario(Document doc) { //Obtener informacion de un usuario a partir de un documento
		return new Usuario (
				doc.get("_id").toString(),
				doc.getString("nombre"),
				doc.getString("email"),
				doc.getString("rol"));
	}
	
	public Usuario saveUsuario(Usuario usuario) { //Guardar informacion de un usuario en la base de datos
		Document doc = new Document();
		doc.append("nombre", usuario.getNombre());
		doc.append("email", usuario.getEmail());
		doc.append("rol", usuario.getRol());
		usuarios.insertOne(doc);
		return usuario(doc);
	}
	
	public boolean isAlumno(String email) { //Comprobar si una entrada es un alumno
		Document elegido = usuarios.find(
				Filters.eq("email", email)).first();
		String rol = elegido.getString("rol");
		return rol.equals("estudiante");	
	}
	
	public ArrayList<Usuario> getAllEstudiantes() { //Obtener todos los estudiantes
		ArrayList<Usuario> allEstudiantes = new ArrayList<Usuario>();
		for(Document doc : usuarios.find(Filters.eq("rol","estudiante"))) {
			allEstudiantes.add(usuario(doc));
		}
		return allEstudiantes;
	}
	
	public ArrayList<Usuario> getAllProfesores() { //Obtener todos los profesores
		ArrayList<Usuario> allEstudiantes = new ArrayList<Usuario>();
		for(Document doc : usuarios.find(Filters.eq("rol","profesor"))) {
			allEstudiantes.add(usuario(doc));
		}
		return allEstudiantes;
	}

	public boolean existUsuario(String email) { //Comprobar si existe un usuario
		Document elegido = usuarios.find(
				Filters.eq("email", email)).first();
		if(elegido != null) {
			return true;
		} else {
			return false;
		}
		
	}
}
