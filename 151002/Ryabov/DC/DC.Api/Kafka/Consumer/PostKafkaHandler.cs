using Forum.Api.Kafka.Messages;

namespace Forum.Api.Kafka.Consumer;

public class PostKafkaHandler : IKafkaHandler<string, KafkaMessage>
{
    private readonly IKafkaMessageBus<string, KafkaMessage> _kafkaMessageBus;

    public PostKafkaHandler(IKafkaMessageBus<string, KafkaMessage> kafkaMessageBus)
    {
        _kafkaMessageBus = kafkaMessageBus;
    }

    public async Task HandleAsync(string key, KafkaMessage value)
    {
        //var post = JsonConvert.DeserializeObject<Post>(value.Data);
        
        await Console.Out.WriteLineAsync(value.MessageType.ToString());
        
        //await Console.Out.WriteLineAsync(JsonConvert.SerializeObject(post));

        /*post.Content = "SecondApi";

        value.Data = JsonConvert.SerializeObject(post);
        
        //await _kafkaMessageBus.PublishAsync(key, value);*/
    }
}