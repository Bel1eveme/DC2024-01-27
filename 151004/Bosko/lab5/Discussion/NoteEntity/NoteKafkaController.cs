using Confluent.Kafka;
using Discussion.Kafka;
using Discussion.NoteEntity.Dto;
using Discussion.NoteEntity.Interface;
using Discussion.NoteEntity.Kafka;
using Newtonsoft.Json;

namespace Discussion.NoteEntity
{
    public class NoteKafkaController(IProducer<string, string> producer, INoteService service)
    {
        private static readonly IConsumer<string, string> _consumer;
        private bool isConsuming = true;

        static NoteKafkaController()
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
                var entity = JsonConvert.DeserializeObject<NoteKafkaRequest>(value);

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

        private async Task<string> GetAll(NoteRequestTO request)
        {
            string response;
            var emptyResponse = new NoteKafkaResponse(
                new NoteResponseTO(
                    default, default, string.Empty, string.Empty),
                State.Decline);
            List<NoteKafkaResponse>? kafkaResponseTo = [emptyResponse];

            try
            {
                var noteResponseList = await service.GetAll();
                kafkaResponseTo = noteResponseList.Select(r => new NoteKafkaResponse(r, State.Approve)).ToList();
            }
            finally
            {
                response = JsonConvert.SerializeObject(kafkaResponseTo);
            }

            return response;
        }

        private async Task<string> GetById(NoteRequestTO request)
        {
            string response;
            var emptyResponse = new NoteKafkaResponse(
                new NoteResponseTO(
                    default, default, string.Empty, string.Empty),
                State.Decline);
            NoteKafkaResponse noteKafkaResponse = emptyResponse;

            try
            {
                var noteResponseTO = await service.GetByID(request.Id);
                noteKafkaResponse = new NoteKafkaResponse(noteResponseTO, State.Approve);
            }
            catch { }
            finally
            {
                response = JsonConvert.SerializeObject(noteKafkaResponse);
            }

            return response;
        }

        private async Task<string> Create(NoteRequestTO request)
        {
            string response;
            var emptyResponse = new NoteKafkaResponse(
                new NoteResponseTO(
                    default, default, string.Empty, string.Empty),
                State.Decline);
            NoteKafkaResponse noteKafkaResponse = emptyResponse;

            try
            {
                var noteResponseTO = await service.Add(request);
                noteKafkaResponse = new NoteKafkaResponse(noteResponseTO, State.Approve);
            }
            finally
            {
                response = JsonConvert.SerializeObject(noteKafkaResponse);
            }

            return response;
        }

        private async Task<string> Update(NoteRequestTO request)
        {
            string response;
            var emptyResponse = new NoteKafkaResponse(
                new NoteResponseTO(
                    default, default, string.Empty, string.Empty),
                State.Decline);
            NoteKafkaResponse noteKafkaResponse = emptyResponse;

            try
            {
                var noteResponseTO = await service.Update(request);
                noteKafkaResponse = new NoteKafkaResponse(noteResponseTO, State.Approve);
            }
            finally
            {
                response = JsonConvert.SerializeObject(noteKafkaResponse);
            }

            return response;
        }

        private async Task<string> Delete(NoteRequestTO request)
        {
            string response;
            var emptyResponse = new NoteKafkaResponse(
                new NoteResponseTO(
                    default, default, string.Empty, string.Empty),
                State.Decline);
            NoteKafkaResponse noteKafkaResponse = emptyResponse;

            try
            {
                var noteResponseTO = await service.Remove(request.Id);
                noteKafkaResponse = new NoteKafkaResponse(
                    new NoteResponseTO(request.Id, default, string.Empty, string.Empty), State.Approve);
            }
            finally
            {
                response = JsonConvert.SerializeObject(noteKafkaResponse);
            }

            return response;
        }
    }
}
