package lanzador;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.annotation.WebServlet;


import com.coxautodev.graphql.tools.SchemaParser;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;
import modelo.Mutation;
import modelo.Query;
import modelo.Tarea;
import modelo.Usuario;
import mongo.TareaRepository;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {
	
	private static TareaRepository tareaRepository;
	private static MongoClient client;
	
	private static void initDB() {
		
		MongoClientURI uri = new MongoClientURI("mongodb://arso:arso-20@cluster0-shard-00-00-slp29.azure.mongodb.net:27017,cluster0-shard-00-01-slp29.azure.mongodb.net:27017,cluster0-shard-00-02-slp29.azure.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority");
		client = new MongoClient(uri);
		
		MongoDatabase mongo = client.getDatabase("Pendientes");
		tareaRepository = new TareaRepository(mongo.getCollection("pendientes"));
		
	}
	
	@Override
	public void destroy() {
		super.destroy();
		client.close();
	}

    public GraphQLEndpoint() {
    	super(buildSchema());  
    	try {
			escucharPeticiones();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private static GraphQLSchema buildSchema() {
    	
    	initDB();
        
        return SchemaParser.newParser()
                .file("schema.graphqls")
                .resolvers(
                  new Query(tareaRepository), 
                  new Mutation(tareaRepository))
                .build()
                .makeExecutableSchema();
    }
    
    private static void escucharPeticiones() throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, TimeoutException {
    	boolean autoAck = false;
    	ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://cikqdgnl:IpTo-xh4cN-UU2Aa0JX2nf1AN3chkLB4@squid.rmq.cloudamqp.com/cikqdgnl");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        final String exchangeName = "arso-exchange";
        final String queueName = "arso-queue";
        final String routingKey = "arso-queue";
        
    	
			channel.basicConsume(queueName, autoAck, "arso-consumidor", new DefaultConsumer(channel) {
			    @Override
			    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
			            byte[] body) throws IOException {
			        
			        String routingKey = envelope.getRoutingKey();
			        String contentType = properties.getContentType();
			        long deliveryTag = envelope.getDeliveryTag();

			        String contenido = new String(body, "UTF-8");
			        
			        
			       
			        ArrayList<Usuario> listaAlumnos = new ArrayList<Usuario>();
			        String url_servicio = "http://localhost:8086/api/";
					Client cliente = Client.create();
					String path = "usuarios" + "?" + "rol=estudiante";
					WebResource recurso = cliente.resource(url_servicio + path);

					ClientResponse respuesta = recurso.method("GET", ClientResponse.class);
					int cabecera = respuesta.getStatus();
					if (cabecera == 200) {
						InputStream a = respuesta.getEntityInputStream();
						JsonReader lector = Json.createReader(a);
						JsonArray lista = lector.readArray();
						for(JsonObject alumno : lista.getValuesAs(JsonObject.class)) {
							String nombre = alumno.getString("nombre");
							String email = alumno.getString("email");
							String rol = alumno.getString("rol");
							Usuario nuevo = new Usuario(nombre,email,rol);
							listaAlumnos.add(nuevo);
						}
					}
					
					if(!listaAlumnos.isEmpty()) {
						
						 JsonReader jsonReader = Json.createReader(new StringReader(contenido));
					     JsonObject objeto = jsonReader.readObject();
					     int tipo = objeto.getInt("tipo");
					     
					     switch (tipo) {
						case 1: //VALOR_CONTROL_PENDIENTE
							tratarPendiente(objeto,listaAlumnos);
								
							break;
							
						case 2: //VALOR_CONTROL_COMPLETADA
							tratarCompletada(objeto);
							break;
							
						case 3: //VALOR_CONTROL_ELIMINAR
							tratarEliminar(objeto);
							break;

						default:
							break;
						}
						
					}
			        
			        
			        channel.basicAck(deliveryTag, false);
			    }
			});
		
    }
    
    private static void tratarPendiente(JsonObject objeto, ArrayList<Usuario> alumnos) {
    	
    	String nombre = objeto.getString("nombre");
    	String identificador = objeto.getString("identificador");
    	String servicio = objeto.getString("servicio");
    	
    	for(Usuario user : alumnos) {
    		Tarea nuevaTarea = new Tarea(nombre,servicio,user.getEmail(),identificador);
    		tareaRepository.saveTarea(nuevaTarea);
    	}	
    }
    
    private static void tratarCompletada(JsonObject objeto) {
    	String email = objeto.getString("email");
    	String identificador = objeto.getString("identificador");
    	String servicio = objeto.getString("servicio");
    	
    	tareaRepository.deleteTarea(email, identificador, servicio);
    	
    }
    
    private static void tratarEliminar(JsonObject objeto) {
    	String identificador = objeto.getString("identificador");
    	String servicio = objeto.getString("servicio");
    	
    	tareaRepository.deleteAllId(identificador,servicio);
    	
    }
    
    
    
    
}
