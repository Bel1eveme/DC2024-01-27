namespace Forum.Api.Kafka.Messages;

public class KafkaMessage
{
    public MessageType MessageType { get; set; }

    public string? Data;
    
    public bool ErrorOccured = false;
    
    public string ErrorMessage = string.Empty;
}