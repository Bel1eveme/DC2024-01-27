using Cassandra;
using Cassandra.Mapping;
using Discussion.NoteEntity;
using ISession = Cassandra.ISession;

namespace Discussion.Storage.Cassandra
{
    public class CassandraStorage
    {
        public readonly ISession Session;
        public readonly Cluster Cluster;
        public readonly IMapper Mapper;

        private const string KeyspaceName = "distcomp";
        private const string TableName = "tbl_notes";

        public CassandraStorage()
        {
            Cluster = Cluster.Builder()
                .AddContactPoint("cassandra")
                .Build();

            Session = Cluster.Connect();

            var createKeyspaceQuery = $"CREATE KEYSPACE IF NOT EXISTS {KeyspaceName} WITH REPLICATION = " +
                $"{{ 'class' : 'SimpleStrategy', 'replication_factor' : 1 }};";

            Session.Execute(createKeyspaceQuery);
            Session.Execute($"USE {KeyspaceName};");

            var tableCreateQuery = new SimpleStatement(
                $"CREATE TABLE IF NOT EXISTS {TableName} (" +
                "id INT," +
                "newsId INT," +
                "country TEXT," +
                "content TEXT," +
                "PRIMARY KEY ((country), id, newsId)" +
                ");"
            );

            Session.Execute(tableCreateQuery);

            Mapper = new Mapper(Session, new MappingConfiguration()
                .Define(new Map<Note>()
                .TableName("tbl_notes")
                .ClusteringKey(u => u.Id)
                .ClusteringKey(u => u.NewsId)
                .PartitionKey(u => u.Country)
                .Column(u => u.Id, c => c.WithName("id"))
                .Column(u => u.NewsId, c => c.WithName("newsId"))
                .Column(u => u.Country, c => c.WithName("country"))
                .Column(u => u.Content, c => c.WithName("content")))
                );
        }
    }
}