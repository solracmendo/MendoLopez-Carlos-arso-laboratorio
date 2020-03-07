//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.3.0-b170531.0717 
// Visite <a href="https://jaxb.java.net/">https://jaxb.java.net/</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2020.03.03 a las 04:21:43 PM CET 
//


package Bookle.bookle.tipos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para TipoTurno complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoTurno"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="reserva" type="{http://www.example.org/bookie}TipoReserva" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="horario" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoTurno", propOrder = {
    "reserva"
})
public class TipoTurno {

    protected TipoReserva reserva;
    @XmlAttribute(name = "horario", required = true)
    protected String horario;

    /**
     * Obtiene el valor de la propiedad reserva.
     * 
     * @return
     *     possible object is
     *     {@link TipoReserva }
     *     
     */
    public TipoReserva getReserva() {
        return reserva;
    }

    /**
     * Define el valor de la propiedad reserva.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoReserva }
     *     
     */
    public void setReserva(TipoReserva value) {
        this.reserva = value;
    }

    /**
     * Obtiene el valor de la propiedad horario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHorario() {
        return horario;
    }

    /**
     * Define el valor de la propiedad horario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHorario(String value) {
        this.horario = value;
    }

}
