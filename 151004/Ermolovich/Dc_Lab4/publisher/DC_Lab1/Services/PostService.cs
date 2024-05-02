using DC_Lab1.DTO.Interface;
using DC_Lab1.DTO;
using Microsoft.EntityFrameworkCore;
using DC_Lab1.Services.Interfaces;
using AutoMapper;
using DC_Lab1.Models;
using Microsoft.AspNetCore.Components.Forms;
using DC_Lab1.DB.BaseDBContext;
using Newtonsoft.Json;
using System.Text;
using Confluent.Kafka;
using StackExchange.Redis;

namespace DC_Lab1.Services
{
    public class PostsService(IMapper _mapper, BaseContext dbContext, IConsumer<Ignore, string> consumer, IProducer<Null, string> producer, IDatabase redisDB) : IPostService
    {
        HttpClient client = new HttpClient();
        public async Task<IResponseTo> CreateEnt(IRequestTo Dto)
        {
            client.BaseAddress = new Uri("http://localhost:24130/");
            string json;
            if (Dto is PostRequestTo postRequest)
            {

                var message = new Message<Null, string> { Value = "Create post" };
                producer.ProduceAsync("InTopic", message).GetAwaiter().GetResult();



                //Thread.Sleep(1000);
                json = $"{{\"{postRequest.Id}\":0,\"storyId\":{postRequest.storyId},\"content\":\"{postRequest.Content}\"}}";


                var stringContent = new StringContent(json, Encoding.UTF8, "application/json");
                var response = await client.PostAsync("api/v1.0/posts", stringContent);

                var consumeResult = consumer.Consume();
                consumer.Commit(consumeResult);
                Console.WriteLine(consumeResult.Value);

                var jsonDeSer = await response.Content.ReadAsStringAsync();
                var postResponse = JsonConvert.DeserializeObject<PostResponseTo>(jsonDeSer);




                if (response.IsSuccessStatusCode)
                {
                    return postResponse;
                }
                else
                {
                    throw new InvalidDataException("Incorrect data");
                }
            }
            throw new InvalidDataException("Incorrect data");

        }

        public async Task DeleteEnt(int id)
        {

            //var message = new Message<Null, string> { Value = "Delete" };
            //producer.ProduceAsync("InTopic", message).GetAwaiter().GetResult();

            client.BaseAddress = new Uri("http://localhost:24130/");
            var response = client.DeleteAsync($"api/v1.0/posts/{id}").Result;

            //var consumeResult = consumer.Consume();
            //consumer.Commit(consumeResult);
            //Console.WriteLine(consumeResult.Value);

            if (response.IsSuccessStatusCode)
            {

                return;
            }
            else
            {
                throw new Exception("Deletting Post ");
            }
        }

        public async Task<IResponseTo> GetEntById(int id)
        {
            //var message = new Message<Null, string> { Value = "GetEntById" };
            //producer.ProduceAsync("InTopic", message).GetAwaiter().GetResult();


            //
            string value = redisDB.StringGet(id.ToString());
            //


            if (value != null)
            {
                Console.WriteLine("Get: " + value);
                var postResponseTo = JsonConvert.DeserializeObject<PostResponseTo>(value);
                return postResponseTo;
            }
            client.BaseAddress = new Uri("http://localhost:24130/");
            var response = await client.GetAsync($"api/v1.0/posts/{id}");

            var jsonDeSer = await response.Content.ReadAsStringAsync();
            var postResponse = JsonConvert.DeserializeObject<PostResponseTo>(jsonDeSer);

            var mappedResponse = _mapper.Map<PostResponseTo>(postResponse);

            //var consumeResult = consumer.Consume();
            //consumer.Commit(consumeResult);
            //Console.WriteLine(consumeResult.Value);

            if (response.IsSuccessStatusCode)
            {
                return mappedResponse;
            }
            else
            {
                throw new ArgumentNullException($"Not found Post: {id}");
            }
        }

        public IEnumerable<IResponseTo> GetAllEnt()
        {
            //var message = new Message<Null, string> { Value = "GetAllEnt" };
            //producer.ProduceAsync("InTopic", message).GetAwaiter().GetResult();

            client.BaseAddress = new Uri("http://localhost:24130/");
            var response = client.GetAsync("api/v1.0/posts").Result;

            var jsonDeSer = response.Content.ReadAsStringAsync().Result;
            var postResponses = JsonConvert.DeserializeObject<List<PostResponseTo>>(jsonDeSer);

            var mappedResponses = postResponses.Select(_mapper.Map<PostResponseTo>);

            //var consumeResult = consumer.Consume();
            //consumer.Commit(consumeResult);
            //Console.WriteLine(consumeResult.Value);

            try
            {
                return mappedResponses;

            }
            catch
            {
                throw new Exception("Getting all Posts exception");
            }
        }

        public async Task<IResponseTo> UpdateEnt(IRequestTo Dto)
        {


            client.BaseAddress = new Uri("http://localhost:24130/");
            string json;
            if (Dto is PostRequestTo postRequest)
            {
                //var message = new Message<Null, string> { Value = "UpdateEnt" };
                //producer.ProduceAsync("InTopic", message).GetAwaiter().GetResult();


                json = $"{{\"id\":{postRequest.Id},\"storyId\":{postRequest.storyId},\"content\":\"{postRequest.Content}\"}}";
                var stringContent = new StringContent(json, Encoding.UTF8, "application/json");
                var response = await client.PutAsync("api/v1.0/posts", stringContent);

                var jsonDeSer = await response.Content.ReadAsStringAsync();
                var postResponse = JsonConvert.DeserializeObject<PostResponseTo>(jsonDeSer);
                var mappedResponse = _mapper.Map<PostResponseTo>(postResponse);

                //var consumeResult = consumer.Consume();
                //consumer.Commit(consumeResult);
                //Console.WriteLine(consumeResult.Value);

                if (response.IsSuccessStatusCode)
                {
                    return mappedResponse;
                }
                else
                {
                    throw new InvalidDataException("Incorrect data for UPDATE Post");
                }
            }
            throw new InvalidDataException("Incorrect data for UPDATE Post");

        }

        public bool Validate(PostRequestTo PostDto)
        {
            if (PostDto?.Content?.Length < 2 || PostDto?.Content?.Length > 2048)
                return false;

            return true;
        }
    }
}
