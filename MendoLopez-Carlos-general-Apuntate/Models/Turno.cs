using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System.Collections.Generic;
using Newtonsoft.Json;

namespace Apuntate.Models{
    public class Turno {
        [BsonElement("turno")]
        [JsonProperty("turno")]
        public string turno{get;set;}

        [BsonElement("actual")]
        [JsonProperty("actual")]
        public string actual{get;set;}

        [BsonElement("listaEspera")]
        [JsonProperty("listaEspera")]
        public List<string> listaEspera{get;set;}

    }
}

