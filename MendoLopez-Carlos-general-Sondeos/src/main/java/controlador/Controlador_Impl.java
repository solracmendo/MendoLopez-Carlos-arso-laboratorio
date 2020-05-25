package controlador;

import java.util.ArrayList;
import java.util.LinkedList;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import Sondeo_Info.Sondeo;
import mongo.SondeoRepository;

public class Controlador_Impl implements Controlador{
	
	private static Controlador_Impl controlador = null;
	
	private static SondeoRepository sondeoRepository = null;
	private static MongoClient client = null;
	
	
	
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
		
		if(pregunta == null || descripcion == null || apertura == null || cierre == null) {
			System.out.println("ALGO HA FALLADOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
		}
		
		Sondeo sondeo = new Sondeo();
		sondeo.setPregunta(pregunta);
		sondeo.setDescripcion(descripcion);
		sondeo.setInicio(apertura);
		sondeo.setFin(cierre);
		sondeo.setMinimo(Integer.valueOf(minimo));
		sondeo.setMaximo(Integer.valueOf(maximo));
		/*
		MongoClientURI uri = new MongoClientURI("mongodb://arso:arso-20@cluster0-shard-00-00-slp29.azure.mongodb.net:27017,cluster0-shard-00-01-slp29.azure.mongodb.net:27017,cluster0-shard-00-02-slp29.azure.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority");
		client = new MongoClient(uri);
		MongoDatabase mong = client.getDatabase("test");
		sondeoRepository = new SondeoRepository(mong.getCollection("sondeos"));
		*/
		Sondeo sondeoFinal = sondeoRepository.saveSondeo(sondeo);	
		
		client.close();
		
		return sondeoFinal.getId();
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
	
	private static void initDB() {
		System.out.println("HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		MongoClientURI uri = new MongoClientURI("mongodb://arso:arso-20@cluster0-shard-00-00-slp29.azure.mongodb.net:27017,cluster0-shard-00-01-slp29.azure.mongodb.net:27017,cluster0-shard-00-02-slp29.azure.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority");
		client = new MongoClient(uri);
		MongoDatabase mong = client.getDatabase("test");
		sondeoRepository = new SondeoRepository(mong.getCollection("sondeos"));
	}
}
