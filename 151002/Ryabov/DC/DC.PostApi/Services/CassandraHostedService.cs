using Cassandra;
using Cassandra.Data.Linq;
using Forum.PostApi.DbProvider;
using Forum.PostApi.Extensions;
using Forum.PostApi.Models;

namespace Forum.PostApi.Services;

public class CassandraHostedService : IHostedService
{
    private readonly ILogger<CassandraHostedService> _logger;
    private readonly ICluster _cluster;
    private readonly IServiceScopeFactory _factory;
    private readonly CassandraOptions _options;

    public CassandraHostedService(ILogger<CassandraHostedService> logger,
        ICluster cluster, IServiceScopeFactory factory, CassandraOptions options)
    {
        _logger = logger;
        _cluster = cluster;
        _factory = factory;
        _options = options;
    }

    public async Task StartAsync(CancellationToken cancellationToken)
    {
        var scope = _factory.CreateScope().ServiceProvider;
        var bootSession = await _cluster.ConnectAsync();
        
        bootSession.CreateKeyspaceIfNotExists(_options.Keyspace);
        _logger.LogInformation("Creating cassandra default keyspace");

        var cassandraProvider = scope.GetRequiredService<ICassandraProvider>();
        
        try
        {
            var session = cassandraProvider.GetSession();
            await new Table<Post>(session).CreateIfNotExistsAsync();
        }
        catch (Exception)
        {
            _logger.LogInformation("Tables creation failed");
            throw;
        }

        var keyspaces = new List<string>(_cluster.Metadata.GetKeyspaces());
        keyspaces.ForEach(value =>
        {
            if (value != _options.Keyspace) return;
            
            _logger.LogInformation("KeySpace: " + value);
            new List<string>(_cluster.Metadata.GetKeyspace(value).GetTablesNames()).ForEach((tableName) =>
            {
                Console.WriteLine("Table: " + tableName);
            });
        });
    }

    public Task StopAsync(CancellationToken cancellationToken)
    {
        _logger.LogInformation("Finishing cassandra background activities");
        return Task.CompletedTask;
    }
}