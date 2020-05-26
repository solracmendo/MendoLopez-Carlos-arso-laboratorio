package controlador;

import java.util.ArrayList;


import Sondeo_Info.Sondeo;

public interface Controlador {
	
	String createSondeo(String titulo, String descripcion, String apertura, String cierre, String minimo, String maximo);
	
	boolean anadirRespuesta(String id, String respuesta);
	
	Sondeo getSondeo(String id);
	
	boolean removeSondeo(String id);

	ArrayList<Sondeo> getAllSondeo();
	
	public boolean contestarPregunta(String id, String pregunta);

}
