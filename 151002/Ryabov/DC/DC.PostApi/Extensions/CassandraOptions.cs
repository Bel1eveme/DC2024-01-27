using Cassandra.Mapping;

namespace Forum.PostApi.Extensions;

public class CassandraOptions
{
    public required string Keyspace { get; set; }
    public required string DefaultPartitionKey { get; set; } 
    public ITypeDefinition[]? Config { get; set; }
}