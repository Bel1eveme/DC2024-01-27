using Confluent.Kafka;
using Confluent.Kafka.SyncOverAsync;
using Discussion.Models.DTOs.Requests;
using Discussion.Models.DTOs.Responses;
using Microsoft.Extensions.Options;
using Publisher.Infrastructure.Redis.Interfaces;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Serializers;
using Publisher.Services.interfaces;
using KafkaException = Publisher.Exceptions.KafkaException;

namespace Publisher.Services;

public class PostService : IPostService, IDisposable
{
    private readonly string _producerTopic;

    //Key is GUID
    private readonly IProducer<string, KafkaRequestDto> _producer;
    private readonly IConsumer<string, KafkaResponseDto> _consumer;
    private readonly ICacheService _cacheService;
    private const string Prefix = "post-";
    
    public PostService(IOptions<ProducerConfig> producerConfig,
        IOptions<ConsumerConfig> consumerConfig,
        IConfiguration configuration, ICacheService cacheService)
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

    public async Task<PostResponseTo> AddAsync(PostRequestTo dto)
    {
        var guid = Guid.NewGuid();
        await _producer.ProduceAsync(_producerTopic,
            new Message<string, KafkaRequestDto>
                { Key = guid.ToString(), Value = new KafkaRequestDto { Method = HttpMethod.Post, Request = dto } });

        CancellationToken cancellationToken = new CancellationTokenSource(TimeSpan.FromSeconds(100)).Token;

        var response = Consume(guid, cancellationToken);

        if (response.Error is not null)
        {
            throw new KafkaException(response.Error.ErrorCode / 100, response.Error);
        }
        
        await _cacheService.SetAsync(Prefix + response.Response!.id, response.Response);
        
        return response.Response!;
    }



    public async Task<PostResponseTo> GetByIdAsync(long id)
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

    public async Task<IEnumerable<PostResponseTo>> GetAllAsync()
    {
        var guid = Guid.NewGuid();
        await _producer.ProduceAsync(_producerTopic,
            new Message<string, KafkaRequestDto>
                { Key = guid.ToString(), Value = new KafkaRequestDto { Method = HttpMethod.Get } });

        CancellationToken cancellationToken = new CancellationTokenSource(TimeSpan.FromSeconds(100)).Token;

        var response = Consume(guid, cancellationToken);

        if (response.Error is not null)
        {
            throw new KafkaException(response.Error.ErrorCode / 100, response.Error);
        }
        
        async void UpdateCache(PostResponseTo post)
        {
            await _cacheService.SetAsync(Prefix + post.id, post);
        }   

        response.ResponseList?.ForEach(UpdateCache);

        return response.ResponseList!;
    }

    public async Task<PostResponseTo> UpdateAsync(PostRequestTo dto)
    {
        var id = dto.id;
        var guid = Guid.NewGuid();
        await _producer.ProduceAsync(_producerTopic,
            new Message<string, KafkaRequestDto>
            {
                Key = guid.ToString(), Value = new KafkaRequestDto { Method = HttpMethod.Put, Id = id, Request = dto }
            });

        CancellationToken cancellationToken = new CancellationTokenSource(TimeSpan.FromSeconds(100)).Token;

        var response = Consume(guid, cancellationToken);

        if (response.Error is not null)
        {
            throw new KafkaException(response.Error.ErrorCode / 100, response.Error);
        }
        
        await _cacheService.SetAsync(Prefix + response.Response!.id, response.Response);
        
        return response.Response!;
    }

    public async Task DeleteAsync(long id)
    {
        var guid = Guid.NewGuid();
        await _producer.ProduceAsync(_producerTopic,
            new Message<string, KafkaRequestDto>
                { Key = guid.ToString(), Value = new KafkaRequestDto { Method = HttpMethod.Delete, Id = id } });

        CancellationToken cancellationToken = new CancellationTokenSource(TimeSpan.FromSeconds(100)).Token;

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