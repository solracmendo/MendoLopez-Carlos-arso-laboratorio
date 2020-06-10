package controlador;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.json.Json;
import javax.json.JsonObject;



import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import Rest.RecursoNoEncontradoException;
import Rest.UsuarioDesautorizadoException;
import Sondeo_Info.Sondeo;
import Sondeo_Info.SondeoException;
import mongo.SondeoRepository;

public class Controlador_Impl implements Controlador {
	
	static final int VALOR_CONTROL_PENDIENTE = 1;
	static final int VALOR_CONTROL_COMPLETADA = 2;
	static final int VALOR_CONTROL_ELIMINAR = 3;

	private static Controlador_Impl controlador = null;

	private static SondeoRepository sondeoRepository = null;
	private static MongoClient client = null;

	private static MongoClientURI uri = null;

	public static Controlador_Impl getControlador() {
		if (controlador == null) {
			controlador = new Controlador_Impl();
			initDB();
		}

		return controlador;
	}

	@Override
	public String createSondeo(String pregunta, String descripcion, String apertura, String cierre, String minimo,
			String maximo, String email) throws SondeoException { //Creacion de un sondeo

		if (pregunta == null || pregunta.equals("")) {
			throw new IllegalArgumentException("El campo pregunta debe contener informacion valida");
		}

		if (descripcion == null || descripcion.equals("")) {
			throw new IllegalArgumentException("El campo descripcion debe contener informacion valida");
		}

		if (apertura == null || apertura.equals("")) {
			throw new IllegalArgumentException("El campo apertura debe contener informacion valida");
		}
		if (cierre == null || cierre.equals("")) {
			throw new IllegalArgumentException("El campo cierre debe contener informacion valida");
		}
		if (minimo == null || minimo.equals("")) {
			throw new IllegalArgumentException("El campo minimo debe contener informacion");
		} else {
			try {
				int i = Integer.parseInt(minimo);
				if (i <= 0) {
					throw new IllegalArgumentException("El minimo debe ser mayor a 0");
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("El minimo debe ser un numero valido");
			}
		}

		if (maximo == null || maximo.equals("")) {
			throw new IllegalArgumentException("El campo maximo debe contener informacion");
		} else {
			try {
				int i = Integer.parseInt(maximo);
				if (i <= 0) {
					throw new IllegalArgumentException("El maximo debe ser mayor a 0");
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("El maximo debe ser un numero valido");
			}
		}
		if (email == null || email.equals("")) {
			throw new IllegalArgumentException("El email debe ser correcto");
		}

		if (isAlumno(email)) {
			throw new UsuarioDesautorizadoException("Un estudiante no puede crear un sondeo");
		}

		if (client == null) {
			iniciarCliente();
		}

		try {

			Sondeo sondeo = new Sondeo();
			sondeo.setPregunta(pregunta);
			sondeo.setDescripcion(descripcion);
			sondeo.setInicio(apertura);
			sondeo.setFin(cierre);
			sondeo.setMinimo(Integer.valueOf(minimo));
			sondeo.setMaximo(Integer.valueOf(maximo));
			Sondeo sondeoFinal = sondeoRepository.saveSondeo(sondeo);
			
			notificarColas(sondeoFinal,email,VALOR_CONTROL_PENDIENTE);
			
			return sondeoFinal.getId();
		} catch (Exception e) {
			throw new SondeoException("Se ha producido un error mientras se registraba el sondeo");
			//e.printStackTrace();
		} finally {
			cerrarCliente();
		}
	}

	@Override
	public boolean anadirRespuesta(String id, String respuesta, String email) throws SondeoException { //Añadir respuesta a un sondeo
		if (id == null || id.equals("")) {
			throw new IllegalArgumentException("El id debe contener informacion valida");
		}

		if (respuesta == null || respuesta.equals("")) {
			throw new IllegalArgumentException("La respuesta debe contener informacion valida");
		}

		if (!existeSondeo(id)) {
			throw new RecursoNoEncontradoException("El sondeo no se ha encontrado");
		}

		if (email == null || email.equals("")) {
			throw new IllegalArgumentException("El email debe ser correcto");
		}

		if (isAlumno(email)) {
			throw new UsuarioDesautorizadoException("Un estudiante no puede anadir una respuesta");
		}

		if (client == null) {
			iniciarCliente();
		}
		try {
			boolean resultado = sondeoRepository.AnadirRespuestaById(id, respuesta);
			return resultado;
		} catch (Exception e) {
			throw new SondeoException("Se ha producido un error al anadir una respuesta");
		} finally {
			cerrarCliente();
		}
	}

	@Override
	public boolean contestarPregunta(String id, String pregunta, String email) throws SondeoException { //Contestar una pregunta por un alumno
		if (id == null || id.equals("")) {
			throw new IllegalArgumentException("El id debe contener informacion valida");
		}

		if (pregunta == null || pregunta.equals("")) {
			throw new IllegalArgumentException("La pregunta debe contener informacion valida");
		}

		if (!existeSondeo(id)) {
			throw new RecursoNoEncontradoException("El sondeo no se ha encontrado");
		}

		if (email == null || email.equals("")) {
			throw new IllegalArgumentException("El email debe ser correcto");
		}

		if (!isAlumno(email)) {
			throw new UsuarioDesautorizadoException("Un profesor no puede contestar");
		}

		if (client == null) {
			iniciarCliente();
		}
		try {
			boolean resultado = sondeoRepository.ResponderRespuestaById(id, pregunta);
			Sondeo sondeo = sondeoRepository.findById(id);
			
			notificarColas(sondeo, email, VALOR_CONTROL_COMPLETADA);
			
			return resultado;
		} catch (Exception e) {
			throw new SondeoException("Se ha producido un error al contestar");
		} finally {
			cerrarCliente();
		}
	}

	@Override
	public Sondeo getSondeo(String id) throws SondeoException { //Obtener informacion de un sondeo
		if (id == null || id.equals("")) {
			throw new IllegalArgumentException("El id debe contener informacion valida");
		}

		if (!existeSondeo(id)) {
			throw new RecursoNoEncontradoException("El sondeo no se ha encontrado");
		}

		if (client == null) {
			iniciarCliente();
		}
		try {
			Sondeo sondeo = sondeoRepository.findById(id);
			return sondeo;
		} catch (Exception e) {
			throw new SondeoException("Se ha producido un error al obtener el sondeo");
		} finally {
			cerrarCliente();
		}
	}

	@Override
	public ArrayList<Sondeo> getAllSondeo() throws SondeoException { //Obtener informacion del listado de todos los sondeos
		if (client == null) {
			iniciarCliente();
		}

		try {
			ArrayList<Sondeo> lista = sondeoRepository.getAllSondeos();
			return lista;
		} catch (Exception e) {
			throw new SondeoException("Se ha producido un fallo durante la toma de sondeos");
		} finally {
			cerrarCliente();
		}
	}

	@Override
	public boolean removeSondeo(String id, String email) throws SondeoException { //Eliminar sondeo

		if (id == null || id.equals("")) {
			throw new IllegalArgumentException("El parametro id debe contener informacion valida");
		}

		if (email == null || email.equals("")) {
			throw new IllegalArgumentException("El email debe ser correcto");
		}

		if (!existeSondeo(id)) {
			throw new RecursoNoEncontradoException("El sondeo no se ha encontrado");
		}

		if (isAlumno(email)) {
			throw new UsuarioDesautorizadoException("Un estudiante no puede eliminar un sondeo");
		}

		if (client == null) {
			iniciarCliente();
		}
		
		try {
			Sondeo sondeo =  sondeoRepository.findById(id);
			boolean resultado = sondeoRepository.deleteById(id);
			notificarColas(sondeo, email, VALOR_CONTROL_ELIMINAR);
			return resultado;
		} catch (Exception e) {
			throw new SondeoException("Se ha producido un error al intentar borrar un sondeo");
		} finally {
			cerrarCliente();
		}
	}

	private boolean existeSondeo(String id) { //Comprobar si existe el sondeo
		if (client == null) {
			iniciarCliente();
		}
		try {
			boolean resultado = sondeoRepository.existId(id);
			return resultado;
		} catch (Exception e) {
			return false;
		} finally {
			cerrarCliente();
		}

	}

	private boolean isAlumno(String email) { //Comprobar si un usuario es Alumno
		String url_servicio = "http://localhost:8086/api/";
		Client cliente = Client.create();
		String path = "usuarios" + "/" + email;
		WebResource recurso = cliente.resource(url_servicio + path);

		ClientResponse respuesta = recurso.method("GET", ClientResponse.class);
		int cabecera = respuesta.getStatus();
		if (cabecera == 200) {
			return true;
		} else {
			return false;
		}

	}

	private static void initDB() { //Inicializar base de datos
		uri = new MongoClientURI(
				"mongodb://arso:arso-20@cluster0-shard-00-00-slp29.azure.mongodb.net:27017,cluster0-shard-00-01-slp29.azure.mongodb.net:27017,cluster0-shard-00-02-slp29.azure.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority");
		client = new MongoClient(uri);
		MongoDatabase mong = client.getDatabase("Sondeos");
		sondeoRepository = new SondeoRepository(mong.getCollection("sondeos"));
	}

	private static void iniciarCliente() { //Inicializar base de datos dentro de cada operacion
		client = new MongoClient(uri);
		MongoDatabase mong = client.getDatabase("test");
		sondeoRepository = new SondeoRepository(mong.getCollection("sondeos"));

	}

	private static void cerrarCliente() { //Cerrar base de datos dentro de cada operacion
		client.close();
		client = null;
		sondeoRepository = null;
	}
	
	private void notificarColas(Sondeo sondeo, String email, int control) throws IOException, TimeoutException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		//Notificar al servicio de Tareas Pendientes los eventos ocurridos durante la ejecucion
		
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setUri("amqp://cikqdgnl:IpTo-xh4cN-UU2Aa0JX2nf1AN3chkLB4@squid.rmq.cloudamqp.com/cikqdgnl");

	    Connection connection = factory.newConnection();

	    Channel channel = connection.createChannel();

	    final String exchangeName = "arso-exchange";
	    final String queueName = "arso-queue";
	    final String routingKey = "arso-queue";
	   
	    try {
	        boolean durable = true;
	        channel.exchangeDeclare(exchangeName, "direct", durable);

	        boolean exclusive = false;
	        boolean autodelete = false;
	        Map<String, Object> properties = null; // sin propiedades
	        channel.queueDeclare(queueName, durable, exclusive, autodelete, properties);    
	        
	        channel.queueBind(queueName, exchangeName, routingKey);
	        
	        String mensaje = null;
	        if(control == VALOR_CONTROL_PENDIENTE) {
	        	mensaje = construirTareaPendiente(sondeo);
	        } else if (control == VALOR_CONTROL_COMPLETADA){
	        	mensaje = construirTareaCompletada(sondeo,email);
	        } else {
	        	mensaje = construirTareaEliminada(sondeo);
	        }
            
	        channel.basicPublish(exchangeName, routingKey, 
	                new AMQP.BasicProperties.Builder()
	                    .contentType("application/json")
	                    .build()                
	                , mensaje.getBytes());
	    } catch (IOException e) {

	        String mensaje = e.getMessage() == null ? e.getCause().getMessage() : e.getMessage();

	        System.out.println("No se ha podido establecer la conexion con el exchange o la cola: \n\t->" + mensaje);
	        
	    } finally {
	    	channel.close();
	    	connection.close();
	    }
	}
	
	private String construirTareaPendiente(Sondeo sondeo) {
		
		JsonObject tarea = Json.createObjectBuilder()
				.add("tipo", VALOR_CONTROL_PENDIENTE)
				.add("nombre", sondeo.getPregunta())
				.add("identificador", sondeo.getId())
				.add("servicio", "sondeos").build();
				
				return tarea.toString();
	}
	
	private String construirTareaCompletada(Sondeo sondeo, String email) {
		JsonObject tarea = Json.createObjectBuilder()
				.add("tipo", VALOR_CONTROL_COMPLETADA)
				.add("email", email)
				.add("identificador", sondeo.getId())
				.add("servicio", "sondeos").build();
				
				return tarea.toString();
	}
	
	private String construirTareaEliminada(Sondeo sondeo) {
		JsonObject tarea = Json.createObjectBuilder()
				.add("tipo", VALOR_CONTROL_ELIMINAR)
				.add("identificador", sondeo.getId())
				.add("servicio", "sondeos").build();
				
				return tarea.toString();
	}

}
