namespace Apuntate.Models{
    public class MensajeCompletado {
        public int tipo {get;set;}
        public string email{get; set;}

        public string identificador{get;set;}
        public string servicio{get;set;}

        public MensajeCompletado(int tipo, string email, string identificador){
            this.tipo = tipo;
            this.email = email;
            this.identificador = identificador;
            this.servicio = "apuntate";
        }
    }
}