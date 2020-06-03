namespace Apuntate.Models{
    public class MensajePendiente{
        public int tipo {get;set;}
        public string nombre{get; set;}

        public string identificador{get;set;}
        public string servicio{get;set;}

        public MensajePendiente (int tipo, string nombre, string identificador){
            this.tipo = tipo;
            this.nombre = nombre;
            this.identificador = identificador;
            this.servicio = "apuntate";
        }



    }
}