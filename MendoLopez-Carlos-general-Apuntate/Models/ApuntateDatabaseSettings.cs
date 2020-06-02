namespace Apuntate.Models{
    public class ApuntateDatabaseSettings : IApuntateDatabaseSettings{
        public string MongoCollectionName {get; set;}

        public string ConnectionString {get; set;}

        public string DatabaseName {get; set;}
    }

    public interface IApuntateDatabaseSettings{
        string MongoCollectionName{get; set;}

        string ConnectionString{get; set;}

        string DatabaseName{get; set;}
    }
}