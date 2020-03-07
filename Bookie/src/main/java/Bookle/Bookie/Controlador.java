package Bookle.Bookie;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import Bookle.bookle.tipos.Actividad;
import Bookle.bookle.tipos.TipoAgenda;
import Bookle.bookle.tipos.TipoReserva;
import Bookle.bookle.tipos.TipoTurno;

@WebService(endpointInterface="Bookle.Bookie.BookleControlador")
public class Controlador implements BookleControlador {

	@Override
	public String createActividad(String titulo, String descripcion, String profesor, String email)
			throws BookleException {
		/**
		 * Método de creación de una actividad. Los parámetros email y descripcion son
		 * opcionales (aceptan valor nulo). El método retorna el id de la nueva
		 * actividad
		 */
		try {
			JAXBContext contexto = JAXBContext.newInstance(Actividad.class);
			Actividad actividad = new Actividad();
			actividad.setTitulo(titulo);
			actividad.setDescripcion(descripcion);
			actividad.setProfesor(profesor);
			actividad.setEmail(email);
			String id = Utils.createId();
			actividad.setId(id);

			TipoAgenda agenda1 = new TipoAgenda();
			agenda1.setFecha(Utils.createFecha(Utils.dateFromString("02-12-2019")));

			TipoTurno turno = new TipoTurno();
			turno.setHorario("10:00-12:00");
			LinkedList<TipoTurno> lista_turno = new LinkedList<TipoTurno>();
			lista_turno.add(turno);
			agenda1.setTurno(lista_turno);
			LinkedList<TipoAgenda> lista_agenda = new LinkedList<TipoAgenda>();
			lista_agenda.add(agenda1);
			actividad.setAgenda(lista_agenda);

			Marshaller marshaller = contexto.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
			marshaller.setProperty("jaxb.schemaLocation", "http://www.example.org/bookie bookie.xsd");
			String nombre = id + ".xml";
			marshaller.marshal(actividad, new File("actividades/" + nombre));

			return id;

		} catch (JAXBException e) {

			throw new BookleException("Error al crear la actividad", e);

		}

	}

	@Override
	public void updateActividad(String id, String titulo, String descripcion, String profesor, String email)
			throws BookleException {
		try {
			JAXBContext contexto = JAXBContext.newInstance(Actividad.class);
			Unmarshaller unmarshaller = contexto.createUnmarshaller();
			String nombre_Documento = "actividades/" + id + ".xml";
			Actividad actividad = (Actividad) unmarshaller.unmarshal(new File(nombre_Documento));
			actividad.setTitulo(titulo);
			actividad.setDescripcion(descripcion);
			actividad.setProfesor(profesor);
			actividad.setEmail(email);

			Marshaller marshaller = contexto.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
			marshaller.setProperty("jaxb.schemaLocation", "http://www.example.org/bookie bookie.xsd");
			marshaller.marshal(actividad, new File(nombre_Documento));

		} catch (JAXBException e) {
			throw new BookleException("Error al actualizar la actividad", e);
		}

	}

	@Override
	public Actividad getActividad(String id) throws BookleException {
		try {
			JAXBContext contexto = JAXBContext.newInstance(Actividad.class);
			Unmarshaller unmarshaller = contexto.createUnmarshaller();
			String nombre_Documento = "actividades/" + id + ".xml";
			Actividad actividad = (Actividad) unmarshaller.unmarshal(new File(nombre_Documento));

			return actividad;

		} catch (JAXBException e) {
			throw new BookleException("Error al obtener la actividad", e);
		}
	}

	@Override
	public boolean removeActividad(String id) throws BookleException {
		try {
			String nombre_Documento = "actividades/" + id + ".xml";
			File fichero = new File(nombre_Documento);
			if (fichero.delete()) {
				return true;
			} else
				return false;

		} catch (Exception e) {
			throw new BookleException("Error al obtener la actividad", e);
		}
	}

	@Override
	public void addDiaActividad(String id, Date fecha, int turnos) throws BookleException {
		try {
			JAXBContext contexto = JAXBContext.newInstance(Actividad.class);
			Unmarshaller unmarshaller = contexto.createUnmarshaller();
			String nombre_Documento = "actividades/" + id + ".xml";
			Actividad actividad = (Actividad) unmarshaller.unmarshal(new File(nombre_Documento));
			List<TipoAgenda> lista_dias = actividad.getAgenda();
			TipoAgenda dia = new TipoAgenda();
			dia.setFecha(Utils.createFecha(fecha));
			LinkedList<TipoTurno> lista_turnos = new LinkedList<TipoTurno>();
			for (int i = 0; i < turnos; i++) {
				TipoTurno turno = new TipoTurno();
				turno.setHorario("xxx");
				lista_turnos.add(turno);
			}
			dia.setTurno(lista_turnos);
			lista_dias.add(dia);
			actividad.setAgenda(lista_dias);

			Marshaller marshaller = contexto.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
			marshaller.setProperty("jaxb.schemaLocation", "http://www.example.org/bookie bookie.xsd");
			marshaller.marshal(actividad, new File(nombre_Documento));

		} catch (JAXBException e) {
			throw new BookleException("Error al añadir el dia la actividad", e);
		}

	}

	@Override
	public boolean removeDiaActividad(String id, Date fecha) throws BookleException {
		try {
			boolean eliminado = false;
			JAXBContext contexto = JAXBContext.newInstance(Actividad.class);
			Unmarshaller unmarshaller = contexto.createUnmarshaller();
			String nombre_Documento = "actividades/" + id + ".xml";
			Actividad actividad = (Actividad) unmarshaller.unmarshal(new File(nombre_Documento));
			List<TipoAgenda> lista_dias = actividad.getAgenda();
			Iterator<TipoAgenda> iterador = lista_dias.iterator();
			/*
			 * for(TipoAgenda dia : lista_dias) {
			 * if(dia.getFecha().compare(Utils.createFecha(fecha)) == 0) {
			 * lista_dias.remove(dia); eliminado = true; } }
			 */
			while (iterador.hasNext()) {
				TipoAgenda agenda = iterador.next();
				if (agenda.getFecha().compare(Utils.createFecha(fecha)) == 0) {
					iterador.remove();
					eliminado = true;
				}
			}
			actividad.setAgenda(lista_dias);
			Marshaller marshaller = contexto.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
			marshaller.setProperty("jaxb.schemaLocation", "http://www.example.org/bookie bookie.xsd");
			marshaller.marshal(actividad, new File(nombre_Documento));
			return eliminado;

		} catch (JAXBException e) {
			throw new BookleException("Error al eliminar el dia la actividad", e);
		}

	}

	@Override
	public int addTurnoActividad(String id, Date fecha) throws BookleException {
		try {

			JAXBContext contexto = JAXBContext.newInstance(Actividad.class);
			Unmarshaller unmarshaller = contexto.createUnmarshaller();
			String nombre_Documento = "actividades/" + id + ".xml";
			Actividad actividad = (Actividad) unmarshaller.unmarshal(new File(nombre_Documento));
			List<TipoAgenda> lista_dias = actividad.getAgenda();
			TipoAgenda dia = null;
			for (TipoAgenda day : lista_dias) {
				if (day.getFecha().compare(Utils.createFecha(fecha)) == 0) {
					dia = day;
				}
			}
			if (dia == null)
				return 0;

			List<TipoTurno> lista_turnos = dia.getTurno();
			TipoTurno turno = new TipoTurno();
			turno.setHorario("yyy");
			lista_turnos.add(turno);
			int indice = lista_turnos.indexOf(turno) + 1;

			dia.setTurno(lista_turnos);
			actividad.setAgenda(lista_dias);

			Marshaller marshaller = contexto.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
			marshaller.setProperty("jaxb.schemaLocation", "http://www.example.org/bookie bookie.xsd");
			marshaller.marshal(actividad, new File(nombre_Documento));

			return indice;

		} catch (JAXBException e) {
			throw new BookleException("Error al añadir el turno al dia", e);
		}

	}

	@Override
	public boolean removeTurnoActividad(String id, Date fecha, int turno) throws BookleException {
		try {

			JAXBContext contexto = JAXBContext.newInstance(Actividad.class);
			Unmarshaller unmarshaller = contexto.createUnmarshaller();
			String nombre_Documento = "actividades/" + id + ".xml";
			Actividad actividad = (Actividad) unmarshaller.unmarshal(new File(nombre_Documento));
			List<TipoAgenda> lista_dias = actividad.getAgenda();
			TipoAgenda dia = null;
			for (TipoAgenda day : lista_dias) {
				if (day.getFecha().compare(Utils.createFecha(fecha)) == 0) {
					dia = day;
				}
			}
			if (dia == null)
				return false;

			List<TipoTurno> lista_turnos = dia.getTurno();
			lista_turnos.remove(turno - 1);
			dia.setTurno(lista_turnos);
			actividad.setAgenda(lista_dias);

			Marshaller marshaller = contexto.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
			marshaller.setProperty("jaxb.schemaLocation", "http://www.example.org/bookie bookie.xsd");
			marshaller.marshal(actividad, new File(nombre_Documento));

			return true;

		} catch (JAXBException e) {
			throw new BookleException("Error al eliminar el turno del dia", e);
		}
	}

	@Override
	public void setHorario(String idActividad, Date fecha, int indice, String horario) throws BookleException {
		try {

			JAXBContext contexto = JAXBContext.newInstance(Actividad.class);
			Unmarshaller unmarshaller = contexto.createUnmarshaller();
			String nombre_Documento = "actividades/" + idActividad + ".xml";
			Actividad actividad = (Actividad) unmarshaller.unmarshal(new File(nombre_Documento));
			List<TipoAgenda> lista_dias = actividad.getAgenda();
			TipoAgenda dia = null;
			for (TipoAgenda day : lista_dias) {
				if (day.getFecha().compare(Utils.createFecha(fecha)) == 0) {
					dia = day;
				}
			}
			if (dia == null)
				return;

			List<TipoTurno> lista_turnos = dia.getTurno();
			TipoTurno turno = lista_turnos.get(indice - 1);
			turno.setHorario(horario);

			Marshaller marshaller = contexto.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
			marshaller.setProperty("jaxb.schemaLocation", "http://www.example.org/bookie bookie.xsd");
			marshaller.marshal(actividad, new File(nombre_Documento));

		} catch (JAXBException e) {
			throw new BookleException("Error al eliminar el turno del dia", e);
		}

	}

	@Override
	public String createReserva(String idActividad, Date fecha, int indice, String alumno, String email)
			throws BookleException {
		try {

			JAXBContext contexto = JAXBContext.newInstance(Actividad.class);
			Unmarshaller unmarshaller = contexto.createUnmarshaller();
			String nombre_Documento = "actividades/" + idActividad + ".xml";
			Actividad actividad = (Actividad) unmarshaller.unmarshal(new File(nombre_Documento));
			List<TipoAgenda> lista_dias = actividad.getAgenda();
			TipoAgenda dia = null;
			for (TipoAgenda day : lista_dias) {
				if (day.getFecha().compare(Utils.createFecha(fecha)) == 0) {
					dia = day;
				}
			}
			if (dia == null)
				return null;

			List<TipoTurno> lista_turnos = dia.getTurno();
			TipoTurno turno = lista_turnos.get(indice - 1);
			TipoReserva reserva = new TipoReserva();
			reserva.setAlumno(alumno);
			reserva.setEmail(email);
			String ident_reserva = Utils.createId();
			reserva.setId(ident_reserva);
			turno.setReserva(reserva);

			Marshaller marshaller = contexto.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
			marshaller.setProperty("jaxb.schemaLocation", "http://www.example.org/bookie bookie.xsd");
			marshaller.marshal(actividad, new File(nombre_Documento));

			return ident_reserva;

		} catch (JAXBException e) {
			throw new BookleException("Error al añadir la reserva", e);
		}
	}

	@Override
	public boolean removeReserva(String idActividad, String ticket) throws BookleException {

		try {
			boolean eliminado = false;
			JAXBContext contexto = JAXBContext.newInstance(Actividad.class);
			Unmarshaller unmarshaller = contexto.createUnmarshaller();
			String nombre_Documento = "actividades/" + idActividad + ".xml";
			Actividad actividad = (Actividad) unmarshaller.unmarshal(new File(nombre_Documento));
			List<TipoAgenda> lista_dias = actividad.getAgenda();

			for (TipoAgenda day : lista_dias) {
				for (TipoTurno turno : day.getTurno()) {
					TipoReserva reserva = turno.getReserva();
					if (reserva != null && reserva.getId().equals(ticket)) {
						{
							turno.setReserva(null);
							eliminado = true;
						}

					}
				}

			}

			Marshaller marshaller = contexto.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
			marshaller.setProperty("jaxb.schemaLocation", "http://www.example.org/bookie bookie.xsd");
			marshaller.marshal(actividad, new File(nombre_Documento));

			return eliminado;

		} catch (JAXBException e) {
			throw new BookleException("Error al eliminar la reserva", e);
		}
	}

	@Override
	public LinkedList<Actividad> getActividades() throws BookleException {
		try {
			// Listar todos los archivos de la carpeta de la forma "*.xml" y obtener las
			// actividades
			LinkedList<Actividad> actividades = new LinkedList<Actividad>();
			JAXBContext contexto = JAXBContext.newInstance(Actividad.class);
			Unmarshaller unmarshaller = contexto.createUnmarshaller();

			File carpeta = new File("actividades");
			String[] lista_archivos = carpeta.list();
			if (lista_archivos == null || lista_archivos.length == 0) {
				return null;
			}
			for (int i = 0; i < lista_archivos.length; i++) {
				Actividad actividad = (Actividad) unmarshaller.unmarshal(new File("actividades/" + lista_archivos[i]));
				actividades.add(actividad);
			}
			return actividades;

		} catch (JAXBException e) {
			throw new BookleException("Error al obtener las actividades", e);
		}
	}

}
