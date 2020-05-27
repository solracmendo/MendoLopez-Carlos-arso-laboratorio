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
	
	private Usuario usuario(Document doc) {
		return new Usuario (
				doc.get("_id").toString(),
				doc.getString("nombre"),
				doc.getString("email"),
				doc.getString("rol"));
	}
	
	public Usuario saveUsuario(Usuario usuario) {
		Document doc = new Document();
		doc.append("nombre", usuario.getNombre());
		doc.append("email", usuario.getEmail());
		doc.append("rol", usuario.getRol());
		usuarios.insertOne(doc);
		return usuario(doc);
	}
	
	public boolean isAlumno(String email) {
		Document elegido = usuarios.find(
				Filters.eq("email", email)).first();
		String rol = elegido.getString("rol");
		return rol.equals("estudiante");	
	}
	
	public ArrayList<Usuario> getAllEstudiantes() {
		ArrayList<Usuario> allEstudiantes = new ArrayList<Usuario>();
		for(Document doc : usuarios.find(Filters.eq("rol","estudiante"))) {
			allEstudiantes.add(usuario(doc));
		}
		return allEstudiantes;
	}
	
	public ArrayList<Usuario> getAllProfesores() {
		ArrayList<Usuario> allEstudiantes = new ArrayList<Usuario>();
		for(Document doc : usuarios.find(Filters.eq("rol","profesor"))) {
			allEstudiantes.add(usuario(doc));
		}
		return allEstudiantes;
	}

	public boolean existUsuario(String email) {
		Document elegido = usuarios.find(
				Filters.eq("email", email)).first();
		if(elegido != null) {
			return true;
		} else {
			return false;
		}
		
	}
}
