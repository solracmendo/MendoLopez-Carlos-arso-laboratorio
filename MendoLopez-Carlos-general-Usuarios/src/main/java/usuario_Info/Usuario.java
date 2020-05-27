package usuario_Info;

public class Usuario {
	
	protected String id;
	protected String nombre;
	protected String email;
	protected String rol;
	
	public Usuario() {}

	public Usuario(String id, String nombre, String email, String rol) {
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.rol = rol;
	}

	public Usuario(String nombre, String email, String rol) {
		this.nombre = nombre;
		this.email = email;
		this.rol = rol;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}
	
	

}
