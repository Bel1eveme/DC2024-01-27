using Cassandra.Mapping;
using Cassandra.Mapping.Attributes;
using Forum.PostApi.Models.Base;

namespace Forum.PostApi.Models;

public class Post : BaseModel<long>
{
    [PartitionKey]
    public string Country { get; set; }
    
    [ClusteringKey]
    public long StoryId { get; set; }
    
    public string? Content { get; set; }
    
    public static Map<Post> GetConfig(string keyspace)
    {
        return new Map<Post>()
            .KeyspaceName(keyspace)
            .TableName("posts")
            .Column(p => p.Country, cm => cm.WithName("country"))
            .PartitionKey(p => p.Country)
            .Column(p => p.StoryId, cm => cm.WithName("storyId"))
            .ClusteringKey(p => p.StoryId)
            .Column(p => p.Id, cm => cm.WithName("id"))
            .ClusteringKey(p => p.Id)
            .Column(p => p.Content, cm => cm.WithName("content"));
    }
}