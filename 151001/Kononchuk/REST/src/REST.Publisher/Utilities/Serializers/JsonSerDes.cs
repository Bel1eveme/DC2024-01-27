using System.Text;
using System.Text.Json;
using Confluent.Kafka;

namespace REST.Publisher.Utilities.Serializers;

public class JsonSerializer<T> : IAsyncSerializer<T> where T : class
{
    Task<byte[]> IAsyncSerializer<T>.SerializeAsync(T data, SerializationContext context)
    {
        string jsonString = JsonSerializer.Serialize(data);
        return Task.FromResult(Encoding.ASCII.GetBytes(jsonString));
    }
}

public class JsonDeserializer<T> : IAsyncDeserializer<T> where T : class
{
    public Task<T> DeserializeAsync(ReadOnlyMemory<byte> data, bool isNull, SerializationContext context)
    {
        string json = Encoding.ASCII.GetString(data.Span);
        return Task.FromResult(JsonSerializer.Deserialize<T>(json) ?? throw new MessageNullException());
    }
}