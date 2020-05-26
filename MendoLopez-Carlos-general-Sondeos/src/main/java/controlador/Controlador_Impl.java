package controlador;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import Sondeo_Info.Sondeo;
import mongo.SondeoRepository;

public class Controlador_Impl implements Controlador{
	
	private static Controlador_Impl controlador = null;
	
	private static SondeoRepository sondeoRepository = null;
	private static MongoClient client = null;
	
	private static MongoClientURI uri = null;
	
	
	
	public static Controlador_Impl getControlador() {
		if(controlador == null) {
			controlador = new Controlador_Impl();
			initDB();
		}
		
		return controlador;
	}

	@Override
	public String createSondeo(String pregunta, String descripcion, String apertura, String cierre, String minimo,
			String maximo) {
		
		if(client == null) {
			iniciarCliente();
		}
		
		Sondeo sondeo = new Sondeo();
		sondeo.setPregunta(pregunta);
		sondeo.setDescripcion(descripcion);
		sondeo.setInicio(apertura);
		sondeo.setFin(cierre);
		sondeo.setMinimo(Integer.valueOf(minimo));
		sondeo.setMaximo(Integer.valueOf(maximo));
		
		Sondeo sondeoFinal = sondeoRepository.saveSondeo(sondeo);	
		
		cerrarCliente();
		
		return sondeoFinal.getId();
	}

	@Override
	public boolean anadirRespuesta(String id, String respuesta) {
		if(client == null) {
			iniciarCliente();
		}
		
		boolean resultado = sondeoRepository.AnadirRespuestaById(id,respuesta);
		
		cerrarCliente();
		return resultado;
	}
	
	@Override
	public boolean contestarPregunta(String id, String pregunta) {
		if(client == null) {
			iniciarCliente();
		}
		
		boolean resultado = sondeoRepository.ResponderRespuestaById(id,pregunta);
		
		cerrarCliente();
		return resultado;
	}

	@Override
	public Sondeo getSondeo(String id) {
		if(client == null) {
			iniciarCliente();
		}
		
		Sondeo sondeo = sondeoRepository.findById(id);
		
		cerrarCliente();
		return sondeo;
	}
	
	@Override
	public ArrayList<Sondeo> getAllSondeo(){
		if(client == null) {
			iniciarCliente();
		}
		
		ArrayList<Sondeo> lista = sondeoRepository.getAllSondeos();
		
		cerrarCliente();
		
		return lista;
	}

	@Override
	public boolean removeSondeo(String id) {
		if(client == null) {
			iniciarCliente();
		}
		
		//NOTIFICAR
		
		boolean resultado = sondeoRepository.deleteById(id);
		cerrarCliente();
		return resultado;
	}
	
	
	
	private static void initDB() {
		uri = new MongoClientURI("mongodb://arso:arso-20@cluster0-shard-00-00-slp29.azure.mongodb.net:27017,cluster0-shard-00-01-slp29.azure.mongodb.net:27017,cluster0-shard-00-02-slp29.azure.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority");
		client = new MongoClient(uri);
		MongoDatabase mong = client.getDatabase("test");
		sondeoRepository = new SondeoRepository(mong.getCollection("sondeos"));
	}
	
	private static void iniciarCliente() {
		client = new MongoClient(uri);
		MongoDatabase mong = client.getDatabase("test");
		sondeoRepository = new SondeoRepository(mong.getCollection("sondeos"));
		
	}
	
	private static void cerrarCliente() {
		client.close();
		client = null;
		sondeoRepository = null;
	}
	
	
}
