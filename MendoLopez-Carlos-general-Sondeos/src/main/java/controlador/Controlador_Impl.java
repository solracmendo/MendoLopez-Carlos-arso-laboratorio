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
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import Rest.RecursoNoEncontradoException;
import Rest.UsuarioDesautorizadoException;
import Sondeo_Info.Sondeo;
import Sondeo_Info.SondeoException;
import mongo.SondeoRepository;

public class Controlador_Impl implements Controlador {

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
			String maximo, String email) throws SondeoException { // CERRAR?

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

		// COMPROBACION USUARIO VALIDO

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
			return sondeoFinal.getId();
		} catch (Exception e) {
			throw new SondeoException("Se ha producido un error mientras se registraba el sondeo");
		} finally {
			cerrarCliente();
		}
	}

	@Override
	public boolean anadirRespuesta(String id, String respuesta, String email) throws SondeoException {
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
	public boolean contestarPregunta(String id, String pregunta, String email) throws SondeoException {
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
			return resultado;
		} catch (Exception e) {
			throw new SondeoException("Se ha producido un error al contestar");
		} finally {
			cerrarCliente();
		}
	}

	@Override
	public Sondeo getSondeo(String id) throws SondeoException {
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
	public ArrayList<Sondeo> getAllSondeo() throws SondeoException {
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
	public boolean removeSondeo(String id, String email) throws SondeoException {

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

		// NOTIFICAR
		try {
			boolean resultado = sondeoRepository.deleteById(id);
			return resultado;
		} catch (Exception e) {
			throw new SondeoException("Se ha producido un error al intentar borrar un sondeo");
		} finally {
			cerrarCliente();
		}
	}

	private boolean existeSondeo(String id) {
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

	private boolean isAlumno(String email) {
		String url_servicio = "http://localhost:8086/api/";
		Client cliente = Client.create();
		String path = "usuarios" + "/" + email;
		WebResource recurso = cliente.resource(url_servicio + path);

		ClientResponse respuesta = recurso.method("GET", ClientResponse.class);
		int cabecera = respuesta.getStatus();
		if (cabecera == 200) {
			return true;
		} else { // Si no es alumno o no está
			return false;
		}

	}

	private static void initDB() {
		uri = new MongoClientURI(
				"mongodb://arso:arso-20@cluster0-shard-00-00-slp29.azure.mongodb.net:27017,cluster0-shard-00-01-slp29.azure.mongodb.net:27017,cluster0-shard-00-02-slp29.azure.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority");
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
