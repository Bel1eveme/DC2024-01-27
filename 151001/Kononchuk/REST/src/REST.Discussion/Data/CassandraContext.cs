using Cassandra;
using Cassandra.Mapping;
using REST.Discussion.Data.Configurations;
using ISession = Cassandra.ISession;

namespace REST.Discussion.Data;

public class CassandraContext
{
    private readonly ISession _session;

    public ISession Session => _session;

    static CassandraContext()
    {
        MappingConfiguration.Global.Define<NoteMappings>();   
    }
    
    public CassandraContext(string? host, string? keyspace, int port = 9042)
    {
        ArgumentNullException.ThrowIfNull(host);
        ArgumentNullException.ThrowIfNull(keyspace);
        var cluster = Cluster.Builder()
            .AddContactPoint(host)
            .WithPort(port)
            .Build();

        try
        {
            _session = cluster.Connect(keyspace);
        }
        catch (InvalidQueryException)
        {
            _session = cluster.Connect();
            _session.CreateKeyspace(keyspace);
            _session.ChangeKeyspace(keyspace);
        }
    }
}