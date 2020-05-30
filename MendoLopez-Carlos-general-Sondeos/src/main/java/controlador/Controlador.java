package controlador;

import java.util.ArrayList;


import Sondeo_Info.Sondeo;
import Sondeo_Info.SondeoException;

public interface Controlador {
	
	String createSondeo(String titulo, String descripcion, String apertura, String cierre, String minimo, String maximo, String email) throws SondeoException;
	
	boolean anadirRespuesta(String id, String respuesta, String email) throws SondeoException;
	
	Sondeo getSondeo(String id) throws SondeoException;
	
	boolean removeSondeo(String id, String email) throws SondeoException;

	ArrayList<Sondeo> getAllSondeo() throws SondeoException;
	
	public boolean contestarPregunta(String id, String pregunta, String email) throws SondeoException;

}
