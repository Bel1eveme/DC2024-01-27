using DC_Lab1.DB.BaseDBContext;
using Microsoft.EntityFrameworkCore;
using Cassandra;
using Cassandra.Mapping;
using Microsoft.Extensions.Hosting;
using DC_Lab1.Models;

namespace DC_Lab1.DB
{
    public class CassandraStorage : BaseContext
    {
        public readonly Cassandra.ISession Session;
        public readonly Cluster Cluster;
        public readonly IMapper Mapper;

        private const string KeyspaceName = "distcomp";
        private const string TableName = "tbl_Post";

        public CassandraStorage(DbContextOptions<BaseContext> options)
            : base(options)
        { }

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
                "storyId INT," +
                "Content TEXT," +
                ");"
            );

            Session.Execute(tableCreateQuery);

            Mapper = new Mapper(Session, new MappingConfiguration()
                .Define(new Map<Post>()
                .TableName("tbl_Post")
                .ClusteringKey(u => u.Id)
                .ClusteringKey(u => u.storyId)
                .Column(u => u.Id, c => c.WithName("id"))
                .Column(u => u.storyId, c => c.WithName("storyId"))
                .Column(u => u.Content, c => c.WithName("Content")))
                );
        }
    }
}