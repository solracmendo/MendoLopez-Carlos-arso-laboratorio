package Bookle.Cliente;

import javax.xml.datatype.XMLGregorianCalendar;

import Bookle.Bookie.Utils;

public class MainCliente {
	public static void main(String[] args) {
		ControladorService servicio = new ControladorService();
		BookleControlador puerto = servicio.getControladorPort();
		
		String id;
		try {
			id = puerto.createActividad("Ejemplo_cliente", "Examen ejemplo_cliente", "Sebastian", "sebas@um.es");
			System.out.println(id);
			puerto.updateActividad(id, "Actual_Act", "Actividad actualizada", "Juanma", "juanma@um.es");
			/*if(puerto.removeActividad(id)) {
			System.out.println("Actividad eliminada en cliente");
			}
			 */
			XMLGregorianCalendar fecha_ejemplo1 = Utils.createFecha(Utils.dateFromString("03-12-2019"));
			XMLGregorianCalendar fecha_ejemplo2 = Utils.createFecha(Utils.dateFromString("02-12-2019"));
			fecha_ejemplo1.setTime(0, 0, 0);
			fecha_ejemplo2.setTime(0, 0, 0);
			puerto.addDiaActividad(id, fecha_ejemplo1, 2);
			if(puerto.removeDiaActividad(id, fecha_ejemplo1)) {
				System.out.println("Dia eliminado en cliente");
			}
			
			int indice_turno = puerto.addTurnoActividad(id, fecha_ejemplo2);
			System.out.println(indice_turno);
			
			puerto.setHorario(id, fecha_ejemplo2, indice_turno, "Turno de mañana");
			
			String id_reserva = puerto.createReserva(id, fecha_ejemplo2, indice_turno, "Javier", "javier@um.es");
			
			if(puerto.removeReserva(id, id_reserva)) {
				System.out.println("Reserva eliminada desde cliente");
			}
			/*
			for(Actividad a : puerto.getActividades()) {
				System.out.println(a.toString());
			}
			*/
			
		} catch (BookleException_Exception e) {
			e.printStackTrace();
		}
		

		
		/*
		for(Actividad a : puerto.getActividades()) {
			System.out.println(a.toString());
		}
		*/
	}
	
}