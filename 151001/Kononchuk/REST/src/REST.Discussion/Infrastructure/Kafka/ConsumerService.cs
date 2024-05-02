using Confluent.Kafka;
using Confluent.Kafka.SyncOverAsync;
using Microsoft.Extensions.Options;
using REST.Discussion.Models.DTOs.Request;
using REST.Discussion.Utilities.Serializers;

namespace REST.Discussion.Infrastructure.Kafka;

public class ConsumerService(
    IOptions<ConsumerConfig> consumerConfig,
    IConfiguration configuration,
    ILogger<ConsumerService> logger,
    MessageProcessor messageProcessor) : BackgroundService
{
    private readonly string _consumerTopic = configuration["Kafka:Consumer:Topic"] ??
                                             throw new InvalidOperationException(
                                                 "configuration[\"Kafka:Consumer:Topic\"] doesn't exist");
    
    private readonly IConsumer<string, KafkaRequestDto> _consumer =
        new ConsumerBuilder<string, KafkaRequestDto>(consumerConfig.Value)
            .SetValueDeserializer(new JsonDeserializer<KafkaRequestDto>().AsSyncOverAsync()).Build();

    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        await Task.Yield();
        _consumer.Subscribe(_consumerTopic);

        while (!stoppingToken.IsCancellationRequested)
        {
            try
            {
                var message = _consumer.Consume(stoppingToken);
                logger.LogInformation($"Received Message {message?.Message.Key}");
                if (message is not null)
                {
                    await messageProcessor.Process(message, stoppingToken);
                }
            }
            catch (Exception ex)
            {
                logger.LogError($"Error processing Kafka message: {ex.Message}");
            }
        }
    }

    public override void Dispose()
    {
        messageProcessor.Dispose();
        _consumer.Dispose();
        base.Dispose();
    }
}