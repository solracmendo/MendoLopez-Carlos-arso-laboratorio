package modelo;

public class Tarea {
	
	private final String nombre;
	private String id;
	private final String identificador_Personal;
	private final String servicio;
	private final String email;
	
	public Tarea(String nombre, String servicio, String email, String identificador_Personal) {
		this.nombre = nombre;
		this.servicio = servicio;
		this.email = email;
		this.identificador_Personal = identificador_Personal;
	}

	public Tarea(String id, String nombre, String identificador_Personal, String servicio, String email) {
		this.nombre = nombre;
		this.id = id;
		this.identificador_Personal = identificador_Personal;
		this.servicio = servicio;
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public String getId() {
		return id;
	}

	public String getServicio() {
		return servicio;
	}

	public String getEmail() {
		return email;
	}

	public String getIdentificador_Personal() {
		return identificador_Personal;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
	
	
	
	
	
	

}
