using Confluent.Kafka;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Kafka.Model;
using System.Net;

namespace Publisher.Controllers.V1_0
{
    [Route("api/v1.0/posts")]
    [ApiController]
    public class PostController(IProducer<string, string> producer) : Controller
    {
        private static readonly IConsumer<string, string> _consumer;
        private const string SrcTopic = "OutTopic";
        private const string DstTopic = "InTopic";

        static PostController()
        {
            var consumerConfig = new ConsumerConfig
            {
                BootstrapServers = "kafka:9092",
                GroupId = "publisher",
                AutoOffsetReset = AutoOffsetReset.Earliest,
                EnableAutoCommit = true,
                EnableAutoOffsetStore = true
            };

            _consumer = new ConsumerBuilder<string, string>(consumerConfig).Build();
            _consumer.Subscribe(SrcTopic);
        }

        [HttpGet]
        [Route("{id:int}")]
        public async Task<JsonResult> GetByID([FromRoute] int id)
        {
            var key = Guid.NewGuid().ToString();
            var value = new PostKafkaRequest(new PostRequestTO(id, default, string.Empty), HttpMethod.Get);
            await producer.ProduceAsync(DstTopic,
                new Message<string, string> { Key = key, Value = JsonConvert.SerializeObject(value) });
            producer.Flush(TimeSpan.FromSeconds(1));

            var json = GetBrokerMessage(key);

            var response = JsonConvert.DeserializeObject<PostKafkaResponse>(json);
            var statusCode =
                response is not null && response.State == State.Approve ? HttpStatusCode.OK : HttpStatusCode.NotFound;

            return new JsonResult(response?.ResponseTO)
            {
                StatusCode = (int)statusCode
            };
        }

        [HttpGet]
        public async Task<IActionResult> Read()
        {
            var key = Guid.NewGuid().ToString();
            var value = new PostKafkaRequest(new PostRequestTO(default, default, string.Empty), HttpMethod.Get);
            await producer.ProduceAsync(DstTopic,
                new Message<string, string> { Key = key, Value = JsonConvert.SerializeObject(value) });
            producer.Flush(TimeSpan.FromSeconds(1));

            var json = GetBrokerMessage(key);
            var response = JsonConvert.DeserializeObject<PostKafkaResponse[]>(json);

            bool okResult = false;
            if (response is not null)
            {
                if (response.Length != 0)
                {
                    okResult = response[0].State == State.Approve;
                }
                else
                {
                    okResult = true;
                }
            }

            var statusCode = okResult ? HttpStatusCode.OK : HttpStatusCode.BadRequest;

            return new JsonResult(response?.Select(r => r.ResponseTO).ToList())
            {
                StatusCode = (int)statusCode
            };
        }

        [HttpPost]
        public async Task<JsonResult> Create([FromBody] PostRequestTO request)
        {
            var key = Guid.NewGuid().ToString();
            var value = new PostKafkaRequest(request, HttpMethod.Post);
            await producer.ProduceAsync(DstTopic,
                new Message<string, string> { Key = key, Value = JsonConvert.SerializeObject(value) });

            var json = GetBrokerMessage(key);
            var response = JsonConvert.DeserializeObject<PostKafkaResponse>(json);

            var statusCode =
                response is not null && response.State == State.Approve ? HttpStatusCode.Created : HttpStatusCode.NotFound;

            return new JsonResult(response?.ResponseTO)
            {
                StatusCode = (int)statusCode
            };
        }

        [HttpPut]
        public async Task<JsonResult> Update([FromBody] PostRequestTO request)
        {
            var key = Guid.NewGuid().ToString();
            var value = new PostKafkaRequest(request, HttpMethod.Put);
            await producer.ProduceAsync(DstTopic,
                new Message<string, string> { Key = key, Value = JsonConvert.SerializeObject(value) });

            var json = GetBrokerMessage(key);
            var response = JsonConvert.DeserializeObject<PostKafkaResponse>(json);

            var statusCode =
                response is not null && response.State == State.Approve ? HttpStatusCode.OK : HttpStatusCode.NotFound;

            return new JsonResult(response?.ResponseTO)
            {
                StatusCode = (int)statusCode
            };
        }

        [HttpDelete]
        [Route("{id:int}")]
        public async Task<IActionResult> Delete([FromRoute] int id)
        {
            var key = Guid.NewGuid().ToString();
            var value = new PostKafkaRequest(new PostRequestTO(id, default, string.Empty), HttpMethod.Delete);
            await producer.ProduceAsync(DstTopic,
                new Message<string, string> { Key = key, Value = JsonConvert.SerializeObject(value) });

            var json = GetBrokerMessage(key);
            var response = JsonConvert.DeserializeObject<PostKafkaResponse>(json);

            var statusCode =
                response is not null && response.State == State.Approve ? HttpStatusCode.NoContent : HttpStatusCode.BadRequest;

            return new StatusCodeResult((int)statusCode);
        }

        private static string GetBrokerMessage(string key)
        {
            ConsumeResult<string, string> res;
            do
            {
                res = _consumer.Consume();
            } while (!res.Message.Key.Equals(key));

            return res.Message.Value;
        }
    }
}
