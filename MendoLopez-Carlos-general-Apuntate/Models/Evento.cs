using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System.Collections.Generic;
using Newtonsoft.Json;

namespace Apuntate.Models
{
    public class Evento{
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string Id {get; set;}

        [BsonElement("titulo")]
        [JsonProperty("titulo")]
        public string Titulo {get; set;}

        [BsonElement("organizacion")]
        [JsonProperty("organizacion")]
        public string Organizacion {get; set;}

        [BsonElement("ubicacion")]
        [JsonProperty("ubicacion")]
        public string Ubicacion {get;set;}

        [BsonElement("categoria")]
        [JsonProperty("categoria")]
        public string Categoria {get;set;}

        [BsonElement("descripcion")]
        [JsonProperty("descripcion")]
        public string Descripcion {get; set;}

        [BsonElement("hora_inicio")]
        [JsonProperty("hora_inicio")]
        public string horaInicio {get; set;}

        [BsonElement("hora_fin")]
        [JsonProperty("hora_fin")]
        public string horaFin{get; set;}

        [BsonElement("apertura")]
        [JsonProperty("apertura")]
        public string Apertura{get; set;}

        [BsonElement("cierre")]
        [JsonProperty("cierre")]
        public string Cierre{get;set;}

        [BsonElement("tipo")]
        [JsonProperty("tipo")]
        public string Tipo{get;set;}

        [BsonElement("frecuencia")]
        [JsonProperty("frecuencia")]
        public string Frecuencia{get;set;}

        [BsonElement("grupos")]
        [JsonProperty("grupos")]
        public List<Turno> grupos{get;set;}
    }
}


