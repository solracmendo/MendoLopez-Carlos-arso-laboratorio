package bookle.controlador;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class ItemLista {
	private String url;
	private String titulo;
	
	public ItemLista(String url, String titulo) {
		this.url = url;
		this.titulo = titulo;
	}

	public String getUrl() {
		return url;
	}

	public String getTitulo() {
		return titulo;
	}
	
	
}
