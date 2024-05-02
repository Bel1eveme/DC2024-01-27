using Confluent.Kafka;
using Discussion.Kafka;
using Discussion.CommentEntity.Dto;
using Discussion.CommentEntity.Interface;
using Discussion.CommentEntity.Kafka;
using Newtonsoft.Json;

namespace Discussion.CommentEntity
{
    public class CommentKafkaController(IProducer<string, string> producer, ICommentService service)
    {
        private static readonly IConsumer<string, string> _consumer;
        private bool isConsuming = true;

        static CommentKafkaController()
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
                var entity = JsonConvert.DeserializeObject<CommentKafkaRequest>(value);

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

        private async Task<string> GetAll(CommentRequestTO request)
        {
            string response;
            var emptyResponse = new CommentKafkaResponse(
                new CommentResponseTO(
                    default, default, string.Empty, string.Empty),
                State.Decline);
            List<CommentKafkaResponse>? kafkaResponseTo = [emptyResponse];

            try
            {
                var commentResponseList = await service.GetAll();
                kafkaResponseTo = commentResponseList.Select(r => new CommentKafkaResponse(r, State.Approve)).ToList();
            }
            finally
            {
                response = JsonConvert.SerializeObject(kafkaResponseTo);
            }

            return response;
        }

        private async Task<string> GetById(CommentRequestTO request)
        {
            string response;
            var emptyResponse = new CommentKafkaResponse(
                new CommentResponseTO(
                    default, default, string.Empty, string.Empty),
                State.Decline);
            CommentKafkaResponse commentKafkaResponse = emptyResponse;

            try
            {
                var commentResponseTO = await service.GetByID(request.Id);
                commentKafkaResponse = new CommentKafkaResponse(commentResponseTO, State.Approve);
            }
            catch { }
            finally
            {
                response = JsonConvert.SerializeObject(commentKafkaResponse);
            }

            return response;
        }

        private async Task<string> Create(CommentRequestTO request)
        {
            string response;
            var emptyResponse = new CommentKafkaResponse(
                new CommentResponseTO(
                    default, default, string.Empty, string.Empty),
                State.Decline);
            CommentKafkaResponse commentKafkaResponse = emptyResponse;

            try
            {
                var commentResponseTO = await service.Add(request);
                commentKafkaResponse = new CommentKafkaResponse(commentResponseTO, State.Approve);
            }
            finally
            {
                response = JsonConvert.SerializeObject(commentKafkaResponse);
            }

            return response;
        }

        private async Task<string> Update(CommentRequestTO request)
        {
            string response;
            var emptyResponse = new CommentKafkaResponse(
                new CommentResponseTO(
                    default, default, string.Empty, string.Empty),
                State.Decline);
            CommentKafkaResponse commentKafkaResponse = emptyResponse;

            try
            {
                var commentResponseTO = await service.Update(request);
                commentKafkaResponse = new CommentKafkaResponse(commentResponseTO, State.Approve);
            }
            finally
            {
                response = JsonConvert.SerializeObject(commentKafkaResponse);
            }

            return response;
        }

        private async Task<string> Delete(CommentRequestTO request)
        {
            string response;
            var emptyResponse = new CommentKafkaResponse(
                new CommentResponseTO(
                    default, default, string.Empty, string.Empty),
                State.Decline);
            CommentKafkaResponse commentKafkaResponse = emptyResponse;

            try
            {
                var commentResponseTO = await service.Remove(request.Id);
                commentKafkaResponse = new CommentKafkaResponse(
                    new CommentResponseTO(request.Id, default, string.Empty, string.Empty), State.Approve);
            }
            finally
            {
                response = JsonConvert.SerializeObject(commentKafkaResponse);
            }

            return response;
        }
    }
}
