namespace Apuntate.Models {

    public class MensajeEliminar {
        public int tipo {get;set;}
        public string identificador{get;set;}
        public string servicio{get;set;}

        public MensajeEliminar(int tipo, string identificador){ //Mensaje de eliminacion de elemento
            this.tipo = tipo;
            this.identificador = identificador;
            this.servicio = "apuntate";
        }


    }
}