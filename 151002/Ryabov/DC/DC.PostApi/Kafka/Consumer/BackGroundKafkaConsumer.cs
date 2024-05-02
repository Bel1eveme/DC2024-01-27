using Confluent.Kafka;
using Confluent.Kafka.Admin;
using Microsoft.Extensions.Options;

namespace Forum.PostApi.Kafka.Consumer
{
    public class BackGroundKafkaConsumer<TK, TV> : BackgroundService
    {
        private readonly KafkaConsumerConfig<TK, TV> _config;
        private IKafkaHandler<TK, TV> _handler;
        private readonly IServiceScopeFactory _serviceScopeFactory;

        public BackGroundKafkaConsumer(IOptions<KafkaConsumerConfig<TK, TV>> config,
            IServiceScopeFactory serviceScopeFactory)
        {
            _serviceScopeFactory = serviceScopeFactory;
            _config = config.Value;
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            await Task.Yield();
            
            using var adminClient = new AdminClientBuilder(new AdminClientConfig { BootstrapServers = _config.BootstrapServers }).Build();
            try
            {
                await adminClient.CreateTopicsAsync(new[] {
                    new TopicSpecification { Name = _config.Topic, NumPartitions = 1, ReplicationFactor = 1 }
                });
                Console.WriteLine($"Topic {_config.Topic} created successfully.");
            }
            catch (CreateTopicsException e) when (e.Results.Select(r => r.Error.Code).Any(el => el == ErrorCode.TopicAlreadyExists))
            {
                Console.WriteLine($"Topic {_config.Topic} already exists.");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"An error occurred creating topic {_config.Topic}: {ex.Message}");

                throw new Exception("GG");
            }

            using var scope = _serviceScopeFactory.CreateScope();
            _handler = scope.ServiceProvider.GetRequiredService<IKafkaHandler<TK, TV>>();

            var builder = new ConsumerBuilder<TK, TV>(_config).SetValueDeserializer(new KafkaDeserializer<TV>());

            using IConsumer<TK, TV> consumer = builder.Build();
            consumer.Subscribe(_config.Topic);

            while (!stoppingToken.IsCancellationRequested)
            {
                var result = consumer.Consume(TimeSpan.FromMilliseconds(1000));

                if (result == null) continue;
                
                await _handler.HandleAsync(result.Message.Key, result.Message.Value);

                consumer.Commit(result);

                consumer.StoreOffset(result);
            }
        }
    }
}