using AutoMapper;
using Confluent.Kafka;
using Microsoft.Extensions.Options;
using REST.Discussion.Models.DTOs.Request;
using REST.Discussion.Models.DTOs.Response;
using REST.Discussion.Models.Entities;
using REST.Discussion.Services.Interfaces;
using REST.Discussion.Utilities.Exceptions;
using REST.Discussion.Utilities.Serializers;

namespace REST.Discussion.Infrastructure.Kafka;

public class MessageProcessor(
    IOptions<ProducerConfig> producerConfig,
    INoteService noteService,
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
            switch (message.Message.Value.Method.Method)
            {
                case "GET":
                    if (value.Id is not null)
                    {
                        logger.LogInformation($"Get {value.Id} request");
                        responseDto.Response = await noteService.GetByIdAsync(new NoteKey { Id = value.Id });
                    }
                    else
                    {
                        logger.LogInformation("Get All request");
                        responseDto.ResponseList = (await noteService.GetAllAsync()).ToList();
                    }

                    break;
                case "POST":
                    logger.LogInformation("Post request");
                    responseDto.Response = await noteService.CreateAsync(value.Request!);
                    break;
                case "PUT":
                    logger.LogInformation($"Put {value.Id} request");
                    responseDto.Response = await noteService.UpdateAsync(value.Request!);
                    break;
                case "DELETE":
                    logger.LogInformation($"Delete {value.Id} request");
                    await noteService.DeleteAsync(new NoteKey { Id = value.Id });
                    break;
            }
        }
        catch (Exception exception)
        {
            responseDto.Response = null;
            responseDto.ResponseList = null;
            switch (exception)
            {
                case ResourceNotFoundException notFoundException:
                    responseDto.Error = mapper.Map<ErrorResponseDto>(notFoundException);
                    break;
                case ValidationException validationException:
                    responseDto.Error = mapper.Map<ErrorResponseDto>(validationException);
                    break;
                case AssociationException associationException:
                    responseDto.Error = mapper.Map<ErrorResponseDto>(associationException);
                    break;
                default:
                    throw;
            }
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