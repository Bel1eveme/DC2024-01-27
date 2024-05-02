using Cassandra;
using Cassandra.Mapping;
using Forum.PostApi.DbProvider;
using Forum.PostApi.Models;
using Forum.PostApi.Services;
using Microsoft.Extensions.Options;

namespace Forum.PostApi.Extensions;

public static class CassandraExtension
{
    public static void RegisterCassandra(this IServiceCollection services)
    {
        var config = services.BuildServiceProvider().GetRequiredService<IConfiguration>();
        var cassandraConfig = config.GetSection("Cassandra");
        services.Configure<CassandraConfig>(cassandraConfig);
        
        var keyspace = config.GetSection("CassandraKeyspaces:PostKeyspace:Name").Value;
        
        if (string.IsNullOrEmpty(keyspace))
        {
            throw new InvalidOperationException("Empty key-value pair: 'CassandraKeyspaces:PostKeyspace:Name'.");
        }
        
        var defaultPartitionKey = config.GetSection("CassandraKeyspaces:PostKeyspace:PartitionKey").Value;
        
        if (string.IsNullOrEmpty(defaultPartitionKey))
        {
            throw new InvalidOperationException("Empty key-value pair: 'CassandraKeyspaces:PostKeyspace:PartitionKey'.");
        }
        
        CassandraOptions cassandraOptions = new()
        {
            DefaultPartitionKey = defaultPartitionKey,
            Keyspace = keyspace,
            Config =
            [
                Post.GetConfig(keyspace),
            ]
        };
        services.AddSingleton(cassandraOptions);
        
        services.AddSingleton<ICluster>(provider => {
            var conf = provider.GetRequiredService<IOptions<CassandraConfig>>();
            var queryOptions = new QueryOptions().SetConsistencyLevel(ConsistencyLevel.One);

            return Cluster.Builder()
                .AddContactPoint(conf.Value.Host)
                .WithPort(conf.Value.Port)
                .WithQueryOptions(queryOptions)
                .WithRetryPolicy(new LoggingRetryPolicy(new DefaultRetryPolicy()))
                .Build();
        });
        
        MappingConfiguration.Global.Define(cassandraOptions.Config);
        services.AddHostedService<CassandraHostedService>();
        
        services.AddScoped<ICassandraProvider, CassandraProvider>();
    }   
}