package controlador;

import java.util.LinkedList;

import Sondeo_Info.Sondeo;
import Sondeo_Info.Utils;

public class Controlador_Impl implements Controlador{
	
	private static Controlador_Impl controlador = null;
	
	public static Controlador_Impl getControlador() {
		if(controlador == null) {
			controlador = new Controlador_Impl();
		}
		
		return controlador;
	}

	@Override
	public String createSondeo(String pregunta, String descripcion, String apertura, String cierre, int minimo,
			int maximo) {
		Sondeo sondeo = new Sondeo();
		sondeo.setPregunta(pregunta);
		sondeo.setDescripcion(descripcion);
		sondeo.setInicio(apertura);
		sondeo.setFin(cierre);
		sondeo.setMinimo(minimo);
		sondeo.setMaximo(maximo);
		sondeo.setRespuestas(new LinkedList<String>());
		
		
		String id = Utils.createId();
		
		sondeo.setId(id);
		
		//REGISTRAR EN BASE DE DATOS
		//ENVIAR DATOS A LAS COLAS
		
		return id;
	}

	@Override
	public String anadirRespuesta(String id, String respuesta) {
		//OBTENER SONDEO SEGUN LA ID
		//ANADIR RESPUESTA
		//ACTUALIZAR
		//ENVIAR MENSAJE A LAS COLAS
		return null;
	}

	@Override
	public Sondeo getSondeo(String id) {
		//OBTENER SONDEO POR LA ID
		//DEVOLVER SONDEO
		return null;
	}

	@Override
	public boolean removeSondeo(String id) {
		//ELIMINAR SONDEO SEGUN LA ID
		//ENVIAR MENSAJE A LAS COLAS??
		return false;
	}

}
