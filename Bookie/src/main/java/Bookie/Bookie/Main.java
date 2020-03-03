package Bookie.Bookie;

import Bookie.bookle.tipos.Actividad;

public class Main {
	
	
	public static void main(String[] args) {
		Controlador controlador = new Controlador();
		try {
			String id = controlador.createActividad("abcd", "Examen abcd", "Juanjo", "juanjo@um.es");
			System.out.println(id);
			
			controlador.updateActividad(id, "Actual_Act", "Actividad actualizada", "Juanma", null);
			
			/*if(controlador.removeActividad(id)) {
				System.out.println("Actividad eliminada");
			}
			*/
			controlador.addDiaActividad(id, Utils.dateFromString("03-12-2019"), 2);
			if(controlador.removeDiaActividad(id, Utils.dateFromString("03-12-2019"))){
				System.out.println("Dia eliminado");
			}
			int indice_turno = controlador.addTurnoActividad(id, Utils.dateFromString("02-12-2019"));
			System.out.println(indice_turno);
			
			controlador.setHorario(id, Utils.dateFromString("02-12-2019"), indice_turno, "JAJAJA");
			
			String id_reserva = controlador.createReserva(id, Utils.dateFromString("02-12-2019"), indice_turno, "Carlos", "carlos.mendol@um.es");
			if(controlador.removeReserva(id, id_reserva)) {
				System.out.println("Reserva eliminada");
			}
			
			for(Actividad a : controlador.getActividades()) {
				System.out.println(a.toString());
			}
			
			
		} catch (BookleException e) {
			
			e.printStackTrace();
		}
	}
}
