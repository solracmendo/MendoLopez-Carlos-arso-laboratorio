package Bookle.ServicioWeb;

import javax.xml.ws.Endpoint;

import Bookle.Bookie.Controlador;


public class DespliegueServicioWeb {
	public static void main(String[] args) {
		Endpoint.publish("http://localhost:9999/ws/bookle", new Controlador());
		System.out.println("Servicio DESPLEGADO");
	}
	
}
