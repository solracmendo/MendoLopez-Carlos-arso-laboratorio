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
            _eventos.Find(evento =>true).ToList();

        public Evento Get(string id) => 
            _eventos.Find<Evento>(evento => evento.Id == id).FirstOrDefault();

        public Evento Create(Evento evento){
            _eventos.InsertOne(evento);
            return evento;
        }

        public void Update(string id, Evento eventoMod) =>
            _eventos.ReplaceOne(evento=>evento.Id == id, eventoMod);

        public void Remove (Evento eventoMod) =>
            _eventos.DeleteOne(evento => evento.Id == eventoMod.Id);

        public void Remove(string id) =>
            _eventos.DeleteOne(evento => evento.Id == id);
    }
}