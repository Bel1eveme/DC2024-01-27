using Confluent.Kafka;
using Confluent.Kafka.SyncOverAsync;
using Microsoft.Extensions.Options;
using REST.Publisher.Infrastructure.Redis.Interfaces;
using REST.Publisher.Models.DTOs.Request;
using REST.Publisher.Models.DTOs.Response;
using REST.Publisher.Services.Interfaces;
using REST.Publisher.Utilities.Serializers;
using KafkaException = REST.Publisher.Utilities.Exceptions.KafkaException;

namespace REST.Publisher.Services.Implementations;

public class NoteService : INoteService, IDisposable
{
    private readonly ICacheService _cacheService;
    private const string Prefix = "note-";

    private readonly string _producerTopic;

    //Key is GUID
    private readonly IProducer<string, KafkaRequestDto> _producer;
    private readonly IConsumer<string, KafkaResponseDto> _consumer;

    public NoteService(IOptions<ProducerConfig> producerConfig,
        IOptions<ConsumerConfig> consumerConfig,
        IConfiguration configuration,
        ICacheService cacheService)
    {
        _cacheService = cacheService;
        _producerTopic = configuration["Kafka:Producer:Topic"] ??
                         throw new InvalidOperationException(
                             "configuration[\"Kafka:Producer:Topic\"] doesn't exist");
        var consumerTopic = configuration["Kafka:Consumer:Topic"] ??
                            throw new InvalidOperationException(
                                "configuration[\"Kafka:Consumer:Topic\"] doesn't exist");

        _producer = new ProducerBuilder<string, KafkaRequestDto>(producerConfig.Value)
            .SetValueSerializer(new JsonSerializer<KafkaRequestDto>()).Build();

        _consumer = new ConsumerBuilder<string, KafkaResponseDto>(consumerConfig.Value)
            .SetValueDeserializer(new JsonDeserializer<KafkaResponseDto>().AsSyncOverAsync()).Build();
        _consumer.Subscribe(consumerTopic);
    }

    public async Task<NoteResponseDto> CreateAsync(NoteRequestDto dto)
    {
        var guid = Guid.NewGuid();
        await _producer.ProduceAsync(_producerTopic,
            new Message<string, KafkaRequestDto>
                { Key = guid.ToString(), Value = new KafkaRequestDto { Method = HttpMethod.Post, Request = dto } });

        CancellationToken cancellationToken = new CancellationTokenSource(TimeSpan.FromSeconds(1)).Token;

        var response = Consume(guid, cancellationToken);

        if (response.Error is not null)
        {
            throw new KafkaException(response.Error.ErrorCode / 100, response.Error);
        }

        await _cacheService.SetAsync(Prefix + response.Response!.Id, response.Response);

        return response.Response!;
    }

    public async Task<NoteResponseDto> GetByIdAsync(long id)
    {
        return await _cacheService.GetAsync(Prefix + id, async () =>
        {
            var guid = Guid.NewGuid();
            await _producer.ProduceAsync(_producerTopic,
                new Message<string, KafkaRequestDto>
                    { Key = guid.ToString(), Value = new KafkaRequestDto { Method = HttpMethod.Get, Id = id } });

            CancellationToken cancellationToken = new CancellationTokenSource(TimeSpan.FromSeconds(1)).Token;

            var response = Consume(guid, cancellationToken);

            if (response.Error is not null)
            {
                throw new KafkaException(response.Error.ErrorCode / 100, response.Error);
            }

            return response.Response!;
        });
    }

    public async Task<IEnumerable<NoteResponseDto>> GetAllAsync()
    {
        var guid = Guid.NewGuid();
        await _producer.ProduceAsync(_producerTopic,
            new Message<string, KafkaRequestDto>
                { Key = guid.ToString(), Value = new KafkaRequestDto { Method = HttpMethod.Get } });

        CancellationToken cancellationToken = new CancellationTokenSource(TimeSpan.FromSeconds(1)).Token;

        var response = Consume(guid, cancellationToken);

        if (response.Error is not null)
        {
            throw new KafkaException(response.Error.ErrorCode / 100, response.Error);
        }

        async void UpdateCache(NoteResponseDto note)
        {
            await _cacheService.SetAsync(Prefix + note.Id, note);
        }   

        response.ResponseList?.ForEach(UpdateCache);

        return response.ResponseList!;
    }

    public async Task<NoteResponseDto> UpdateAsync(long id, NoteRequestDto dto)
    {
        var guid = Guid.NewGuid();
        await _producer.ProduceAsync(_producerTopic,
            new Message<string, KafkaRequestDto>
            {
                Key = guid.ToString(), Value = new KafkaRequestDto { Method = HttpMethod.Put, Id = id, Request = dto }
            });

        CancellationToken cancellationToken = new CancellationTokenSource(TimeSpan.FromSeconds(1)).Token;

        var response = Consume(guid, cancellationToken);

        if (response.Error is not null)
        {
            throw new KafkaException(response.Error.ErrorCode / 100, response.Error);
        }

        await _cacheService.SetAsync(Prefix + response.Response!.Id, response.Response);

        return response.Response!;
    }

    public async Task DeleteAsync(long id)
    {
        var guid = Guid.NewGuid();
        await _producer.ProduceAsync(_producerTopic,
            new Message<string, KafkaRequestDto>
                { Key = guid.ToString(), Value = new KafkaRequestDto { Method = HttpMethod.Delete, Id = id } });

        CancellationToken cancellationToken = new CancellationTokenSource(TimeSpan.FromSeconds(1)).Token;

        var response = Consume(guid, cancellationToken);

        if (response.Error is not null)
        {
            throw new KafkaException(response.Error.ErrorCode / 100, response.Error);
        }

        await _cacheService.RemoveAsync(Prefix + id);
    }

    private KafkaResponseDto Consume(Guid guid, CancellationToken cancellationToken)
    {
        Message<string, KafkaResponseDto> message;

        do
        {
            message = _consumer.Consume(cancellationToken).Message;
        } while (message.Key != guid.ToString());

        return message.Value;
    }

    public void Dispose()
    {
        _producer.Dispose();
        _consumer.Dispose();
    }
}