using Apuntate.Models;
using MongoDB.Driver;
using System.Collections.Generic;
using System.Linq;

namespace Apuntate.Services{
    public class ApuntateService{
        private readonly IMongoCollection<Evento> _eventos;

        public ApuntateService(IApuntateDatabaseSettings settings){
            var client = new MongoClient(settings.ConnectionString);
            var database = client.GetDatabase(settings.DatabaseName);

            _eventos = database.GetCollection<Evento>(settings.MongoCollectionName);
        }

        public List<Evento> Get() => 
            _eventos.Find(evento =>true).ToList(); //Operacion para obtener todos los elementos de la base de datos

        public Evento Get(string id) => 
            _eventos.Find<Evento>(evento => evento.Id == id).FirstOrDefault(); //Obtener un elemento de la base de datos

        public Evento Create(Evento evento){ //Creacion de un evento en la base de datos
            _eventos.InsertOne(evento);
            return evento;
        }

        public void Update(string id, Evento eventoMod) => //Actualizacion de un evento
            _eventos.ReplaceOne(evento=>evento.Id == id, eventoMod);

        public void Remove (Evento eventoMod) => //Eliminar un evento de la base de datos
            _eventos.DeleteOne(evento => evento.Id == eventoMod.Id);

        public void Remove(string id) => //Eliminar un evento de la base de datos a través del id
            _eventos.DeleteOne(evento => evento.Id == id);
    }
}