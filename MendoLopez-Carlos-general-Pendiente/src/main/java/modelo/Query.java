package modelo;

import java.util.List;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import mongo.TareaRepository;

public class Query implements GraphQLRootResolver{
	private final TareaRepository tareaRepository;
	
	public Query(TareaRepository tareaRepository) {
		this.tareaRepository = tareaRepository;
	}
	
	public List<Tarea> allTareas() {
		return tareaRepository.getAllTareas();
	}
	
	public List<Tarea> misTareas(String email){
		return tareaRepository.getMyTareas(email);
	}
}
