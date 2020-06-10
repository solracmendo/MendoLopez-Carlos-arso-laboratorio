package modelo;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import mongo.TareaRepository;

public class Query implements GraphQLRootResolver{ //Operaciones QUERY
	private final TareaRepository tareaRepository;
	
	public Query(TareaRepository tareaRepository) {
		this.tareaRepository = tareaRepository;
	}
	
	public List<Tarea> allTareas() { //Consulta de todas las tareas
		return tareaRepository.getAllTareas();
	}
	
	public List<Tarea> misTareas(String email){ //Consulta de las tareas de un usuario
		return tareaRepository.getMyTareas(email);
	}
}
