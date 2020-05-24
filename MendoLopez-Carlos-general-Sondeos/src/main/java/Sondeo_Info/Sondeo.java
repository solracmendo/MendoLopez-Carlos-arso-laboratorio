package Sondeo_Info;


import java.util.List;


public class Sondeo {
	protected String id;
	protected String pregunta;
	protected String descripcion;
	protected List<String> respuestas;
	protected String inicio;
	protected String fin;
	protected Integer minimo;
	protected Integer maximo;

	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public List<String> getRespuestas() {
		return respuestas;
	}


	public void setRespuestas(List<String> respuestas) {
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
