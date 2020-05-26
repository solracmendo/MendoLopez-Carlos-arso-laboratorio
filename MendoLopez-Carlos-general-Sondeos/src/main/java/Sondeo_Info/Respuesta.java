package Sondeo_Info;

public class Respuesta {
	protected String nombre;
	protected Integer cantidad;
	
	public Respuesta(String nombre, Integer cantidad) {
		super();
		this.nombre = nombre;
		this.cantidad = cantidad;
	}
	
	public Respuesta(String nombre) {
		this.nombre = nombre;
		this.cantidad = 0;
	}
	
	public Respuesta() {}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	
	
}
