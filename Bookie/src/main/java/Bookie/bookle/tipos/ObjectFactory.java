//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.3.0-b170531.0717 
// Visite <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.03.03 a las 04:21:43 PM CET 
//


package Bookie.bookle.tipos;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the bookle.tipos package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: bookle.tipos
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Actividad }
     * 
     */
    public Actividad createActividad() {
        return new Actividad();
    }

    /**
     * Create an instance of {@link TipoAgenda }
     * 
     */
    public TipoAgenda createTipoAgenda() {
        return new TipoAgenda();
    }

    /**
     * Create an instance of {@link TipoTurno }
     * 
     */
    public TipoTurno createTipoTurno() {
        return new TipoTurno();
    }

    /**
     * Create an instance of {@link TipoReserva }
     * 
     */
    public TipoReserva createTipoReserva() {
        return new TipoReserva();
    }

}
