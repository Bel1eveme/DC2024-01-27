using Cassandra;
using Forum.PostApi.Extensions;

namespace Forum.PostApi.DbProvider;

public class CassandraProvider : ICassandraProvider
{
    private readonly ILogger<CassandraProvider> _logger;
    private readonly ICluster _cluster;
    private readonly IClusterSession _session;

    public CassandraProvider(ILogger<CassandraProvider> logger, ICluster cluster, CassandraOptions options)
    {
        _logger = logger;
        _cluster = cluster;
        _session = _cluster.Connect(options.Keyspace);
    }

    public IClusterSession GetSession() => _session;

    public IClusterSession GetSession(string keyspace)
    {
        try
        {
            _cluster.Connect(keyspace);
        }
        catch (Exception)
        {
            _logger.LogCritical("Error connecting to cassandra session");
            throw;
        }

        return _session;
    }

    public async ValueTask DisposeAsync()
    {
        await _session.ShutdownAsync();
    }
}