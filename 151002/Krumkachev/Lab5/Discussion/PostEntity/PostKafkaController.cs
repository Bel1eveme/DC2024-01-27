using Confluent.Kafka;
using Discussion.Kafka;
using Discussion.PostEntity.Dto;
using Discussion.PostEntity.Interface;
using Discussion.PostEntity.Kafka;
using Newtonsoft.Json;

namespace Discussion.PostEntity
{
    public class PostKafkaController(IProducer<string, string> producer, IPostService service)
    {
        private static readonly IConsumer<string, string> _consumer;
        private bool isConsuming = true;

        static PostKafkaController()
        {
            var consumerConfig = new ConsumerConfig
            {
                BootstrapServers = "kafka:9092",
                GroupId = "discussion",
                AutoOffsetReset = AutoOffsetReset.Earliest,
                EnableAutoCommit = true,
                EnableAutoOffsetStore = true
            };
            _consumer = new ConsumerBuilder<string, string>(consumerConfig).Build();
            _consumer.Subscribe("InTopic");

            var producerConfig = new ProducerConfig
            {
                BootstrapServers = "kafka:9092"
            };
        }

        public async Task StartConsuming()
        {
            while (isConsuming)
            {
                var res = _consumer.Consume();
                var key = res.Message.Key;
                var value = res.Message.Value;
                var entity = JsonConvert.DeserializeObject<PostKafkaRequest>(value);

                if (entity is null)
                {
                    continue;
                }

                var requestTo = entity.RequestTO;
                switch (entity.Method)
                {
                    case HttpMethod m when m == HttpMethod.Get && entity.RequestTO.Id == default:
                        value = await GetAll(requestTo);
                        break;
                    case HttpMethod m when m == HttpMethod.Get:
                        value = await GetById(requestTo);
                        break;
                    case HttpMethod m when m == HttpMethod.Post:
                        value = await Create(requestTo);
                        break;
                    case HttpMethod m when m == HttpMethod.Put:
                        value = await Update(requestTo);
                        break;
                    case HttpMethod m when m == HttpMethod.Delete:
                        value = await Delete(requestTo);
                        break;
                    default:
                        break;
                }

                await producer.ProduceAsync("OutTopic", new Message<string, string>
                {
                    Key = key,
                    Value = value
                });
                producer.Flush(TimeSpan.FromSeconds(1));
            }
        }

        public void StopConsuming()
        {
            isConsuming = false;
        }

        private async Task<string> GetAll(PostRequestTO request)
        {
            string response;
            var emptyResponse = new PostKafkaResponse(
                new PostResponseTO(
                    default, default, string.Empty, string.Empty),
                State.Decline);
            List<PostKafkaResponse>? kafkaResponseTo = [emptyResponse];

            try
            {
                var postResponseList = await service.GetAll();
                kafkaResponseTo = postResponseList.Select(r => new PostKafkaResponse(r, State.Approve)).ToList();
            }
            finally
            {
                response = JsonConvert.SerializeObject(kafkaResponseTo);
            }

            return response;
        }

        private async Task<string> GetById(PostRequestTO request)
        {
            string response;
            var emptyResponse = new PostKafkaResponse(
                new PostResponseTO(
                    default, default, string.Empty, string.Empty),
                State.Decline);
            PostKafkaResponse postKafkaResponse = emptyResponse;

            try
            {
                var postResponseTO = await service.GetByID(request.Id);
                postKafkaResponse = new PostKafkaResponse(postResponseTO, State.Approve);
            }
            catch { }
            finally
            {
                response = JsonConvert.SerializeObject(postKafkaResponse);
            }

            return response;
        }

        private async Task<string> Create(PostRequestTO request)
        {
            string response;
            var emptyResponse = new PostKafkaResponse(
                new PostResponseTO(
                    default, default, string.Empty, string.Empty),
                State.Decline);
            PostKafkaResponse postKafkaResponse = emptyResponse;

            try
            {
                var postResponseTO = await service.Add(request);
                postKafkaResponse = new PostKafkaResponse(postResponseTO, State.Approve);
            }
            finally
            {
                response = JsonConvert.SerializeObject(postKafkaResponse);
            }

            return response;
        }

        private async Task<string> Update(PostRequestTO request)
        {
            string response;
            var emptyResponse = new PostKafkaResponse(
                new PostResponseTO(
                    default, default, string.Empty, string.Empty),
                State.Decline);
            PostKafkaResponse postKafkaResponse = emptyResponse;

            try
            {
                var postResponseTO = await service.Update(request);
                postKafkaResponse = new PostKafkaResponse(postResponseTO, State.Approve);
            }
            finally
            {
                response = JsonConvert.SerializeObject(postKafkaResponse);
            }

            return response;
        }

        private async Task<string> Delete(PostRequestTO request)
        {
            string response;
            var emptyResponse = new PostKafkaResponse(
                new PostResponseTO(
                    default, default, string.Empty, string.Empty),
                State.Decline);
            PostKafkaResponse postKafkaResponse = emptyResponse;

            try
            {
                var postResponseTO = await service.Remove(request.Id);
                postKafkaResponse = new PostKafkaResponse(
                    new PostResponseTO(request.Id, default, string.Empty, string.Empty), State.Approve);
            }
            finally
            {
                response = JsonConvert.SerializeObject(postKafkaResponse);
            }

            return response;
        }
    }
}
