namespace Apuntate.Models{
    public class MensajeCompletado {
        public int tipo {get;set;}
        public string email{get; set;}

        public string identificador{get;set;}
        public string servicio{get;set;}

        public MensajeCompletado(int tipo, string email, string identificador){ //Mensaje enviado cuando un alumno se asigna a un turno
            this.tipo = tipo;
            this.email = email;
            this.identificador = identificador;
            this.servicio = "apuntate";
        }
    }
}