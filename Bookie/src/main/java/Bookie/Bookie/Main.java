package Bookie.Bookie;

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
			System.out.println(controlador.addTurnoActividad(id, Utils.dateFromString("02-12-2019")));
		} catch (BookleException e) {
			
			e.printStackTrace();
		}
	}
}
