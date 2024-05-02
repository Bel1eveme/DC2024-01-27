using Cassandra;
using Cassandra.Mapping;
using Discussion.CommentEntity;
using ISession = Cassandra.ISession;

namespace Discussion.Storage.Cassandra
{
    public class CassandraStorage
    {
        public readonly ISession Session;
        public readonly Cluster Cluster;
        public readonly IMapper Mapper;

        private const string KeyspaceName = "distcomp";
        private const string TableName = "tbl_comments";

        public CassandraStorage()
        {
            Cluster = Cluster.Builder()
                .AddContactPoint("cassandra")
                .WithPort(9042)
                .Build();

            Session = Cluster.Connect();

            var createKeyspaceQuery = $"CREATE KEYSPACE IF NOT EXISTS {KeyspaceName} WITH REPLICATION = " +
                $"{{ 'class' : 'SimpleStrategy', 'replication_factor' : 1 }}";

            Session.Execute(createKeyspaceQuery);
            Session.Execute($"USE {KeyspaceName}");

            var tableCreateQuery = new SimpleStatement(
                $"CREATE TABLE IF NOT EXISTS {TableName} (" +
                "id INT," +
                "issueId INT," +
                "country TEXT," +
                "content TEXT," +
                "PRIMARY KEY ((country), id, issueId)" +
                ");"
            );

            Session.Execute(tableCreateQuery);

            Mapper = new Mapper(Session, new MappingConfiguration()
                .Define(new Map<Comment>()
                .TableName("tbl_comments")
                .ClusteringKey(u => u.Id)
                .ClusteringKey(u => u.IssueId)
                .PartitionKey(u => u.Country)
                .Column(u => u.Id, c => c.WithName("id"))
                .Column(u => u.IssueId, c => c.WithName("issueId"))
                .Column(u => u.Country, c => c.WithName("country"))
                .Column(u => u.Content, c => c.WithName("content")))
                );
        }
    }
}