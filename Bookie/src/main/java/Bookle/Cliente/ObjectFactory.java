
package Bookle.Cliente;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the Bookle.Cliente package. 
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

    private final static QName _Actividad_QNAME = new QName("http://www.example.org/bookie", "actividad");
    private final static QName _BookleException_QNAME = new QName("http://Bookie.Bookie/", "BookleException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: Bookle.Cliente
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

    /**
     * Create an instance of {@link BookleException }
     * 
     */
    public BookleException createBookleException() {
        return new BookleException();
    }

    /**
     * Create an instance of {@link LinkedList }
     * 
     */
    public LinkedList createLinkedList() {
        return new LinkedList();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Actividad }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Actividad }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.example.org/bookie", name = "actividad")
    public JAXBElement<Actividad> createActividad(Actividad value) {
        return new JAXBElement<Actividad>(_Actividad_QNAME, Actividad.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BookleException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BookleException }{@code >}
     */
    @XmlElementDecl(namespace = "http://Bookie.Bookie/", name = "BookleException")
    public JAXBElement<BookleException> createBookleException(BookleException value) {
        return new JAXBElement<BookleException>(_BookleException_QNAME, BookleException.class, null, value);
    }

}
