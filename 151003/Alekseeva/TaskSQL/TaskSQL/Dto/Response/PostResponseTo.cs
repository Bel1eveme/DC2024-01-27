using System.Text.Json.Serialization;

namespace TaskSQL.Dto.Response;

public record PostResponseTo(
    [property: JsonPropertyName("id")] long Id,
    [property: JsonPropertyName("tweetId")]
    long TweetId,
    [property: JsonPropertyName("content")]
    string Content
);