package Sondeo_Info;


import java.util.List;


public class Sondeo {
	protected String id;
	protected String pregunta;
	protected String descripcion;
	protected List<Respuesta> respuestas;
	protected String inicio;
	protected String fin;
	protected Integer minimo;
	protected Integer maximo;
	
	public Sondeo() {}

	
	public Sondeo(String pregunta, String descripcion, List<Respuesta> respuestas, String inicio, String fin,
			Integer minimo, Integer maximo) {
		super();
		this.pregunta = pregunta;
		this.descripcion = descripcion;
		this.respuestas = respuestas;
		this.inicio = inicio;
		this.fin = fin;
		this.minimo = minimo;
		this.maximo = maximo;
	}


	public Sondeo(String id, String pregunta, String descripcion, List<Respuesta> respuestas, String inicio, String fin,
			Integer minimo, Integer maximo) {
		super();
		this.id = id;
		this.pregunta = pregunta;
		this.descripcion = descripcion;
		this.respuestas = respuestas;
		this.inicio = inicio;
		this.fin = fin;
		this.minimo = minimo;
		this.maximo = maximo;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public List<Respuesta> getRespuestas() {
		return respuestas;
	}


	public void setRespuestas(List<Respuesta> respuestas) {
		this.respuestas = respuestas;
	}


	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public String getFin() {
		return fin;
	}

	public void setFin(String fin) {
		this.fin = fin;
	}

	public Integer getMinimo() {
		return minimo;
	}

	public void setMinimo(Integer minimo) {
		this.minimo = minimo;
	}

	public Integer getMaximo() {
		return maximo;
	}

	public void setMaximo(Integer maximo) {
		this.maximo = maximo;
	}
	
	

}
