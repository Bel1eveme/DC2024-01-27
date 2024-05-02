using System.ComponentModel.DataAnnotations;
using AutoMapper;
using Confluent.Kafka;
using Discussion.Models.DTOs.Requests;
using Discussion.Models.DTOs.Responses;
using Discussion.Serializers;
using Discussion.Services.interfaces;
using Microsoft.Extensions.Options;

namespace Discussion.Infrastructure.Kafka;

public class MessageProcessor(
    IOptions<ProducerConfig> producerConfig,
    IPostService postService,
    IMapper mapper,
    IConfiguration configuration,
    ILogger<MessageProcessor> logger) : IDisposable
{
    private readonly string _producerTopic = configuration["Kafka:Producer:Topic"] ??
                                             throw new InvalidOperationException(
                                                 "configuration[\"Kafka:Producer:Topic\"] doesn't exist");

    //Key is GUID
    private readonly IProducer<string, KafkaResponseDto> _producer =
        new ProducerBuilder<string, KafkaResponseDto>(producerConfig.Value)
            .SetValueSerializer(new JsonSerializer<KafkaResponseDto>()).Build();

    public async Task Process(ConsumeResult<string, KafkaRequestDto> message, CancellationToken cancellationToken)
    {
        var value = message.Message.Value;
        var responseDto = new KafkaResponseDto();
        try
        {
            long id;
            switch (message.Message.Value.Method.Method)
            {
                case "GET":
                    if (value.Id is not null)
                    {
                        id = (long)value.Id;
                        logger.LogInformation($"Get {value.Id} request");
                        responseDto.Response = await postService.GetByIdAsync(id);
                    }
                    else
                    {
                        logger.LogInformation("Get All request");
                        responseDto.ResponseList = (await postService.GetAllAsync()).ToList();
                    }

                    break;
                case "POST":
                    logger.LogInformation("Post request");
                    responseDto.Response = await postService.AddAsync(value.Request!, value.Request.country);
                    break;
                case "PUT":
                    logger.LogInformation($"Put {value.Id} request");
                    responseDto.Response = await postService.UpdateAsync(value.Request!,value.Request.country);
                    break;
                case "DELETE":
                    logger.LogInformation($"Delete {value.Id} request");
                    id = (long)value.Id;
                    await postService.DeleteAsync(id);
                    break;
            }
        }
        catch (Exception exception)
        {
            
        }

        await _producer.ProduceAsync(_producerTopic,
            new Message<string, KafkaResponseDto>
                { Key = message.Message.Key, Value = responseDto }, cancellationToken);
    }

    public void Dispose()
    {
        _producer.Dispose();
    }
}