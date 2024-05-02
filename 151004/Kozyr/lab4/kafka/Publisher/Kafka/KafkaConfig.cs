using Confluent.Kafka;
using Confluent.Kafka.Admin;

namespace Publisher.Kafka
{
    public static class KafkaConfig
    {
        public static async Task CreateTopicIfNotExists(string servers = "kafka:9092", string topicName = "OutTopic",
            int partitions = 1, short replicationFactor = 1)
        {
            var config = new AdminClientConfig
            {
                BootstrapServers = servers
            };

            using var adminClient = new AdminClientBuilder(config).Build();
            try
            {
                var metadata = adminClient.GetMetadata(TimeSpan.FromSeconds(10));

                var topicSpecification = new TopicSpecification
                {
                    Name = topicName,
                    NumPartitions = partitions,
                    ReplicationFactor = replicationFactor
                };

                await adminClient.CreateTopicsAsync([topicSpecification]);
                Console.WriteLine($"Topic '{topicName}' created");
            }
            catch (CreateTopicsException e)
            {
                Console.WriteLine($"Topic creation error: {e.Results[0].Error.Reason}");
            }
        }
    }
}
