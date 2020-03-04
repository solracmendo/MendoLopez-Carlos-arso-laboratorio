package Bookle.Cliente;

public class MainCliente {
	public static void main(String[] args) {
		//service: ControladorService
		//portType: BookleControlado
		//USAR SETTIME(horas,minutos,segundos)
		
		ControladorService servicio = new ControladorService();
		BookleControlador puerto = servicio.getControladorPort();
		
		try {
			String id = puerto.createActividad("JIJI", "FIN_PRUEBA", "Daniel", "Hola");
			System.out.println(id);
		} catch (BookleException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
