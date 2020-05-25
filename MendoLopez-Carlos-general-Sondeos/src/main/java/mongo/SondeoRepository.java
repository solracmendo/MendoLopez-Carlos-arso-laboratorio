package mongo;



import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.client.MongoCollection;

import Sondeo_Info.Sondeo;

import com.mongodb.client.model.Filters;

public class SondeoRepository {
	
	private final MongoCollection<Document> sondeos;
	
	public SondeoRepository(MongoCollection<Document> sondeos) {
		this.sondeos = sondeos;
	}
	
	private Sondeo sondeo(Document doc) {
		@SuppressWarnings("unchecked")
		ArrayList<String> resps = (ArrayList<String>)doc.get("respuestas");
		/*
		BasicDBList lista = (BasicDBList) doc.get("respuestas");
		List<String> resps = new ArrayList<String>();
		for(Object element : lista) {
			resps.add((String)element);
		}
		*/
		return new Sondeo (
				doc.get("_id").toString(),
				doc.getString("pregunta"),
				doc.getString("descripcion"),
				resps,
				doc.getString("inicio"),
				doc.getString("fin"),
				doc.getInteger("minimo"),
				doc.getInteger("maximo"));
		}
	
	
	public Sondeo saveSondeo(Sondeo sondeo) {
		Document doc = new Document();
		doc.append("pregunta",sondeo.getPregunta());
		doc.append("descripcion", sondeo.getDescripcion());
		doc.append("respuestas",new ArrayList<String>());
		doc.append("inicio", sondeo.getInicio());
		doc.append("fin", sondeo.getFin());
		doc.append("minimo", sondeo.getMinimo());
		doc.append("maximo", sondeo.getMaximo());
		sondeos.insertOne(doc);
		return sondeo(doc);
	}
	
	public Sondeo findById(String id) {
		Document doc = sondeos.find(Filters.eq("_id", new ObjectId(id))).first();
		return sondeo(doc);
	}
	
	public List<Sondeo> getAllSondeos() {
		List<Sondeo> allSondeos = new ArrayList<>();
		for(Document doc : sondeos.find()) {
			allSondeos.add(sondeo(doc));
		}
		return allSondeos;
	}
	
	

}
