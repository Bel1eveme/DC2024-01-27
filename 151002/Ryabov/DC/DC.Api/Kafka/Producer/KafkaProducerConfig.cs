using Confluent.Kafka;

namespace Forum.Api.Kafka.Producer
{
    public class KafkaProducerConfig<Tk, Tv> : ProducerConfig
    {
        public string Topic { get; set; }
    }
}