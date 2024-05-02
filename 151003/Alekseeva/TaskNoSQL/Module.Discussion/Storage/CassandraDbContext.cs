using Cassandra;
using Cassandra.Data.Linq;
using Discussion.Models;
using ISession = Cassandra.ISession;
namespace Discussion.Storage;

public sealed class CassandraDbContext
{
    public Table<Post> Posts { get; set; }

    public CassandraDbContext(CassandraDbOptions options)
    {
        ISession session = Connect(options);
        CreateKeyspace(session, options.Keyspace);
        CreateTable(session);

        Posts = new Table<Post>(session);
    }
    
    private static ISession Connect(CassandraDbOptions options)
    {
        Cluster cluster = Cluster.Builder()
            .AddContactPoint(options.Host)
            .WithPort(options.Port)
            .Build();

        return cluster.Connect();
    }

    private static void CreateKeyspace(ISession session, string keyspace)
    {
        session.Execute($"CREATE KEYSPACE IF NOT EXISTS {keyspace} WITH REPLICATION = " +
            $"{{ 'class' : 'SimpleStrategy', 'replication_factor' : 1 }}");
        session.Execute($"USE {keyspace}");
    }
    
    private static void CreateTable(ISession session)
    {
        session.Execute(new SimpleStatement(
            $"CREATE TABLE IF NOT EXISTS tbl_posts (" +
            "id BIGINT," +
            "tweetId BIGINT," +
            "country TEXT," +
            "content TEXT," +
            "PRIMARY KEY ((country), id, tweetId)" +
            ");"
        ));
    }
}
