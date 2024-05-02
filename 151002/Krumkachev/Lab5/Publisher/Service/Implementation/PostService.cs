using Confluent.Kafka;
using Microsoft.Extensions.Caching.Distributed;
using Newtonsoft.Json;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Kafka.Model;
using Publisher.Service.Interface;

namespace Publisher.Service.Implementation
{
    public class PostService(IProducer<string, string> producer, IDistributedCache cache) : IPostService
    {
        private static readonly IConsumer<string, string> consumer;
        private const string SRC_TOPIC = "OutTopic";
        private const string DST_TOPIC = "InTopic";

        static PostService()
        {
            var consumerConfig = new ConsumerConfig
            {
                BootstrapServers = "kafka:9092",
                GroupId = "publisher",
                AutoOffsetReset = AutoOffsetReset.Earliest,
                EnableAutoCommit = true,
                EnableAutoOffsetStore = true
            };

            consumer = new ConsumerBuilder<string, string>(consumerConfig).Build();
            consumer.Subscribe(SRC_TOPIC);
        }

        public async Task<PostKafkaResponse?> GetByID(int id)
        {
            var cacheResponse = await cache.GetStringAsync(GetRedisId(id));
            if (cacheResponse is null)
            {
                var json = await GetByIdFromBroker(id);
                await cache.SetStringAsync(GetRedisId(id), json);

                return JsonConvert.DeserializeObject<PostKafkaResponse>(json);
            }

            _ = Task.Run(async () =>
            {
                var json = await GetByIdFromBroker(id);
                await cache.SetStringAsync(GetRedisId(id), json);
            });

            return JsonConvert.DeserializeObject<PostKafkaResponse>(cacheResponse);
        }

        private async Task<string> GetByIdFromBroker(int id)
        {
            var key = Guid.NewGuid().ToString();
            var value = new PostKafkaRequest(new PostRequestTO(id, default, string.Empty), HttpMethod.Get);
            await producer.ProduceAsync(DST_TOPIC,
                new Message<string, string> { Key = key, Value = JsonConvert.SerializeObject(value) });
            producer.Flush(TimeSpan.FromSeconds(1));

            return GetBrokerMessage(key);
        }

        public async Task<IList<PostKafkaResponse>> GetAll()
        {
            var key = Guid.NewGuid().ToString();
            var value = new PostKafkaRequest(new PostRequestTO(default, default, string.Empty), HttpMethod.Get);
            await producer.ProduceAsync(DST_TOPIC,
                new Message<string, string> { Key = key, Value = JsonConvert.SerializeObject(value) });
            producer.Flush(TimeSpan.FromSeconds(1));

            var json = GetBrokerMessage(key);
            return JsonConvert.DeserializeObject<IList<PostKafkaResponse>>(json) ?? [];
        }

        public async Task<PostKafkaResponse?> Add(PostRequestTO request)
        {
            var key = Guid.NewGuid().ToString();
            var value = new PostKafkaRequest(request, HttpMethod.Post);
            await producer.ProduceAsync(DST_TOPIC,
                new Message<string, string> { Key = key, Value = JsonConvert.SerializeObject(value) });

            var json = GetBrokerMessage(key);
            var res = JsonConvert.DeserializeObject<PostKafkaResponse>(json);
            if (res is not null)
            {
                await cache.SetStringAsync(GetRedisId(res.ResponseTO.Id), json);
            }

            return res;
        }

        public async Task<PostKafkaResponse?> Update(PostRequestTO request)
        {
            var key = Guid.NewGuid().ToString();
            var value = new PostKafkaRequest(request, HttpMethod.Put);
            await producer.ProduceAsync(DST_TOPIC,
                new Message<string, string> { Key = key, Value = JsonConvert.SerializeObject(value) });

            var json = GetBrokerMessage(key);
            var res = JsonConvert.DeserializeObject<PostKafkaResponse>(json);
            if (res is not null)
            {
                await cache.RemoveAsync(GetRedisId(res.ResponseTO.Id));
                await cache.SetStringAsync(GetRedisId(res.ResponseTO.Id), json);
            }

            return res;
        }

        public async Task<bool> Remove(int id)
        {
            var key = Guid.NewGuid().ToString();
            var value = new PostKafkaRequest(new PostRequestTO(id, default, string.Empty), HttpMethod.Delete);
            await producer.ProduceAsync(DST_TOPIC,
                new Message<string, string> { Key = key, Value = JsonConvert.SerializeObject(value) });

            var json = GetBrokerMessage(key);
            var response = JsonConvert.DeserializeObject<PostKafkaResponse>(json);
            await cache.RemoveAsync(GetRedisId(id));

            return response is not null && response.State == State.Approve;
        }

        private static string GetBrokerMessage(string key)
        {
            ConsumeResult<string, string> res;
            do
            {
                res = consumer.Consume();
            } while (!res.Message.Key.Equals(key));

            return res.Message.Value;
        }

        private static string GetRedisId(int id) => $"Post:{id}";
    }
}
