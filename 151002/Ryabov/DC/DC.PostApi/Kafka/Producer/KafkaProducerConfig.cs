using Confluent.Kafka;

namespace Forum.PostApi.Kafka.Producer
{
    public class KafkaProducerConfig<Tk, Tv> : ProducerConfig
    {
        public string Topic { get; set; }
    }
}