package modelo;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import mongo.TareaRepository;

public class Mutation implements GraphQLRootResolver{ //Clase con operaciones de mutacion (NO UTILIZADA)
	
	private final TareaRepository tareaRepository;
	
	public Mutation(TareaRepository tareaRepository) {
		this.tareaRepository = tareaRepository;
	}
	/*
	  createTarea(nombre: String!, servicio: String!, identificador: String!, email: String!): Tarea
		completarTarea(correo: String!, identificador: String!, servicio: String!) : Boolean
	 
	*/
	public Tarea createTarea(String nombre, String servicio, String identificador, String email) {
		Tarea nueva = new Tarea(nombre,servicio,email,identificador);
		return tareaRepository.saveTarea(nueva);
	}
	
	public Boolean completarTarea(String correo, String identificador, String servicio) {
		return tareaRepository.deleteTarea(correo, identificador, servicio);
	}
	

}
