using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Confluent.Kafka;
using Newtonsoft.Json;
using static Confluent.Kafka.ConfigPropertyNames;
using StackExchange.Redis;

namespace KafkaServer
{
    internal class Program
    {
        static void Main(string[] args)
        {
            HttpClient client = new HttpClient();
            client.BaseAddress = new Uri("http://localhost:24130/");
            var config = new ProducerConfig { BootstrapServers = "localhost:9092" };
            string jsonRes;

            var consumerConfig = new ConsumerConfig
            {
                BootstrapServers = "localhost:9092",
                GroupId = "my-group",
                AutoOffsetReset = AutoOffsetReset.Earliest
            };
            //
            ConnectionMultiplexer redis = ConnectionMultiplexer.Connect("localhost");
            IDatabase redisDB = redis.GetDatabase();
            //

            var consumer = new ConsumerBuilder<Ignore, string>(consumerConfig).Build();
            var producer = new ProducerBuilder<Null, string>(config).Build();
            consumer.Subscribe("InTopic");
            while (true)
            {
                //Принть сообщение от А
                var consumeResult = consumer.Consume();
                consumer.Commit(consumeResult);
                jsonRes = consumeResult.Message.Value;
                Console.WriteLine(jsonRes);
               
                switch (jsonRes)
                {
                    case "Create":
                        consumeResult = consumer.Consume();
                        consumer.Commit(consumeResult);
                        jsonRes = consumeResult.Message.Value;
                        Console.WriteLine(jsonRes);

                        //
                        string valueCr = redisDB.StringGet(jsonRes);
                        //

                        var stringContentC = new StringContent(valueCr, Encoding.UTF8, "application/json");
                        var responseC = client.PostAsync($"api/v1.0/messages", stringContentC).Result;
                        var jsonDeSerC = responseC.Content.ReadAsStringAsync().Result;

                        //
                        redisDB.StringSet(jsonRes, jsonDeSerC);
                        //

                        var messageC = new Message<Null, string> { Value = "Success" };
                        producer.ProduceAsync("OutTopic", messageC).GetAwaiter().GetResult();

                        Console.WriteLine("Send OutTopic Create");
                        break;
                    case "Delete":
                        consumeResult = consumer.Consume();
                        consumer.Commit(consumeResult);
                        jsonRes = consumeResult.Message.Value;

                        var responseD = client.DeleteAsync($"api/v1.0/messages/{int.Parse(jsonRes)}").Result;

                        if (responseD.IsSuccessStatusCode)
                        {
                            var messageD = new Message<Null, string> { Value = "Success" };
                            producer.ProduceAsync("OutTopic", messageD).GetAwaiter().GetResult();
                            Console.WriteLine("Send OutTopic Delete Success");
                        }
                        else
                        {
                            var messageD = new Message<Null, string> { Value = "Failure" };
                            producer.ProduceAsync("OutTopic", messageD).GetAwaiter().GetResult();
                            Console.WriteLine("Send OutTopic Delete Failure");
                        }
                        break;
                    case "GetMessageById":
                        consumeResult = consumer.Consume();
                        consumer.Commit(consumeResult);
                        jsonRes = consumeResult.Message.Value;
                        Console.WriteLine(jsonRes);

                        var responseBI = client.GetAsync($"api/v1.0/messages/{int.Parse(jsonRes)}").Result;
                        var jsonDeSerBI = responseBI.Content.ReadAsStringAsync().Result;

                        var messageBI = new Message<Null, string> { Value = jsonDeSerBI };
                        producer.ProduceAsync("OutTopic", messageBI).GetAwaiter().GetResult();
                        Console.WriteLine("Send OutTopic GetMessageById");

                        break;
                    case "GetAllEnt":

                        var responseGA = client.GetAsync("api/v1.0/messages").Result;
                        var jsonDeSerGA = responseGA.Content.ReadAsStringAsync().Result;

                        var messageGA = new Message<Null, string> { Value = jsonDeSerGA };
                        producer.ProduceAsync("OutTopic", messageGA).GetAwaiter().GetResult();
                        Console.WriteLine("Send OutTopic GetAllEnt");

                        break;
                    case "UpdateMessage":
                        consumeResult = consumer.Consume();
                        consumer.Commit(consumeResult);
                        jsonRes = consumeResult.Message.Value;
                        Console.WriteLine(jsonRes);

                        //
                        string valueUp = redisDB.StringGet(jsonRes);
                        //

                        var stringContentU = new StringContent(valueUp, Encoding.UTF8, "application/json");
                        var responseU = client.PutAsync($"api/v1.0/messages", stringContentU).Result;
                        var jsonDeSerU = responseU.Content.ReadAsStringAsync().Result;

                        //
                        redisDB.StringSet(jsonRes, jsonDeSerU);
                        //

                        var messageU = new Message<Null, string> { Value = "Success" };
                        producer.ProduceAsync("OutTopic", messageU).GetAwaiter().GetResult();
                        Console.WriteLine("Send OutTopic Update");
                        break;
                    default:
                        Console.WriteLine("Unknow message");
                        break;
                }
                Console.WriteLine();
                
            }        
        }
    }
}
