using DC_Lab1.DTO.Interface;
using DC_Lab1.DTO;
using Microsoft.EntityFrameworkCore;
using DC_Lab1.Services.Interfaces;
using AutoMapper;
using DC_Lab1.Models;
using Microsoft.AspNetCore.Components.Forms;
using DC_Lab1.DB.BaseDBContext;
using Humanizer;
using System.Net.Http.Json;
using Newtonsoft.Json;
using System.Text;
using Microsoft.AspNetCore.Mvc;
using System.Net.Http;
using Confluent.Kafka;
using static Confluent.Kafka.ConfigPropertyNames;
using DC_Lab1;
using static System.Runtime.InteropServices.JavaScript.JSType;

namespace DC_Lab1.Services
{
    public class MessagesService(IMapper _mapper, BaseContext dbContext, IConsumer<Ignore, string> consumer, IProducer<Null, string> producer) : IMessageService
    {
        HttpClient client = new HttpClient();
        public async Task<IResponseTo> CreateEnt(IRequestTo Dto)
        {
            string json;
            if (Dto is MessageRequestTo messageRequest)
            {
                var message = new Message<Null, string> { Value = "Create" };
                producer.ProduceAsync("InTopic", message).GetAwaiter().GetResult();
                Thread.Sleep(1000);
                json = $"{{\"Id\":{messageRequest.Id},\"storyId\":{messageRequest.StoryId},\"content\":\"{messageRequest.Content}\"}}";
                message = new Message<Null, string> { Value = json };
                producer.ProduceAsync("InTopic", message).GetAwaiter().GetResult();
                
                var consumeResult = consumer.Consume();
                consumer.Commit(consumeResult);

                string jsonRes = consumeResult.Message.Value;                                    

                var messageResponseTo = JsonConvert.DeserializeObject<MessageResponseTo>(jsonRes);
                return messageResponseTo;

            }
            throw new InvalidDataException("Incorrect data for CREATE Message");
        }

        public async Task DeleteEnt(int id)
        {
            var message = new Message<Null, string> { Value = "Delete" };
            producer.ProduceAsync("InTopic", message).GetAwaiter().GetResult();

            string strNumber = id.ToString();
            message = new Message<Null, string> { Value = strNumber };
            producer.ProduceAsync("InTopic", message).GetAwaiter().GetResult();

            var consumeResult = consumer.Consume();
            consumer.Commit(consumeResult);

            string jsonRes = consumeResult.Message.Value;
            if (jsonRes == "Success")
            {
                return;
            }
            else
            {
                throw new Exception("Deletting Message exception");
            }
        }

        public async Task<IResponseTo> GetEntById(int id)
        {
            var message = new Message<Null, string> { Value = "GetMessageById" };
            producer.ProduceAsync("InTopic", message).GetAwaiter().GetResult();

            string strNumber = id.ToString();
            message = new Message<Null, string> { Value = strNumber };
            producer.ProduceAsync("InTopic", message).GetAwaiter().GetResult();

            var consumeResult = consumer.Consume();
            consumer.Commit(consumeResult);

            string jsonRes = consumeResult.Message.Value;

            var messageResponseTo = JsonConvert.DeserializeObject<MessageResponseTo>(jsonRes);
            return messageResponseTo;
        }

        public IEnumerable<IResponseTo> GetAllEnt()
        {
            var message = new Message<Null, string> { Value = "GetAllEnt" };
            producer.ProduceAsync("InTopic", message).GetAwaiter().GetResult();

            var consumeResult = consumer.Consume();
            consumer.Commit(consumeResult);
            string jsonRes = consumeResult.Message.Value;

            var messageResponses = JsonConvert.DeserializeObject<List<MessageResponseTo>>(jsonRes);

            var mappedResponses = messageResponses.Select(_mapper.Map<MessageResponseTo>);
            try
            {
                return mappedResponses;

            }
            catch
            {
                throw new Exception("Getting all Messages exception");
            }
        }

        public async Task<IResponseTo> UpdateEnt(IRequestTo Dto)
        {
            string json;
            if (Dto is MessageRequestTo messageRequest)
            {
                var message = new Message<Null, string> { Value = "UpdateMessage" };
                producer.ProduceAsync("InTopic", message).GetAwaiter().GetResult();
                Thread.Sleep(1000);
                json = $"{{\"id\":{messageRequest.Id},\"storyId\":{messageRequest.StoryId},\"content\":\"{messageRequest.Content}\"}}";

                message = new Message<Null, string> { Value = json };
                producer.ProduceAsync("InTopic", message).GetAwaiter().GetResult();

                var consumeResult = consumer.Consume();
                consumer.Commit(consumeResult);

                string jsonRes = consumeResult.Message.Value;

                var messageResponseTo = JsonConvert.DeserializeObject<MessageResponseTo>(jsonRes);

                return messageResponseTo;

            }
            throw new InvalidDataException("Incorrect data for UPDATE Message");
        }

        public bool Validate(MessageRequestTo MessageDto)
        {
            if (MessageDto?.Content?.Length < 2 || MessageDto?.Content?.Length > 2048)
                return false;

            return true;
        }
    }
}
