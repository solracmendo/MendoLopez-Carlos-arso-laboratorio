package bookle.controlador;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "actividades")
public class ListadoActividades {

    private LinkedList<ItemLista> actividad;
    
    public ListadoActividades() {
        
        this.actividad = new LinkedList<>();
    }
    
    public LinkedList<ItemLista> getActividad() {
        return actividad;
    }   
}
