using DC_Lab1.DTO.Interface;
using DC_Lab1.DTO;
using Microsoft.EntityFrameworkCore;
using DC_Lab1.Services.Interfaces;
using AutoMapper;
using DC_Lab1.Models;
using Microsoft.AspNetCore.Components.Forms;
using DC_Lab1.DB.BaseDBContext;
using static Confluent.Kafka.ConfigPropertyNames;
using Confluent.Kafka;
using StackExchange.Redis;

namespace DC_Lab1.Services
{
    public class PostsService(IMapper _mapper, BaseContext dbContext, IConsumer<Ignore, string> consumer, IProducer<Null, string> producer, IDatabase redisDB) : IPostService
    {
        public async Task<IResponseTo> CreateEnt(IRequestTo Dto)
        {
            if (Dto is PostRequestTo postRequest)
            {


                var PostDto = (PostRequestTo)Dto;



                if (!Validate(PostDto))
                {
                    throw new InvalidDataException("Incorrect data for CREATE Message");

                }
                var Post = _mapper.Map<Post>(PostDto);
                dbContext.Posts.Add(Post);
                await dbContext.SaveChangesAsync();
                var response = _mapper.Map<PostResponseTo>(Post);

                var consumeResult = consumer.Consume();
                consumer.Commit(consumeResult);
                Console.WriteLine(consumeResult.Value);

                var message = new Message<Null, string> { Value = "OK" };
                producer.ProduceAsync("OutTopic", message).GetAwaiter().GetResult();

                string json;
                json = $"{{\"Id\":{response.Id},\"storyId\":{postRequest.storyId},\"content\":\"{postRequest.Content}\"}}";

                //
                redisDB.StringSet(response.Id.ToString(), json);
                Console.WriteLine("Set: " + json);
                //
                return response;
            }
            return null;
        }

        public async Task DeleteEnt(int id)
        {
            //var consumeResult = consumer.Consume();
            //consumer.Commit(consumeResult);
            //Console.WriteLine(consumeResult.Value);

            //var message = new Message<Null, string> { Value = "Deleted Success" };
            //producer.ProduceAsync("OutTopic", message).GetAwaiter().GetResult();

            try
            {
                var Post = await dbContext.Posts.FindAsync(id);
                dbContext.Posts.Remove(Post!);
                await dbContext.SaveChangesAsync();
                return;
            }
            catch
            {
                throw new Exception("Deletting Post exception");
            }

        }

        public async Task<IResponseTo> GetEntById(int id)
        {

            var Post = _mapper.Map<PostResponseTo>(await dbContext.Posts.FindAsync(id));

            //var consumeResult = consumer.Consume();
            //consumer.Commit(consumeResult);
            //Console.WriteLine(consumeResult.Value);

            //var message = new Message<Null, string> { Value = "GetEntById Success" };
            //producer.ProduceAsync("OutTopic", message).GetAwaiter().GetResult();

            return Post is not null ? _mapper.Map<PostResponseTo>(Post) : throw new ArgumentNullException($"Not found Message: {id}");
        }

        public IEnumerable<IResponseTo> GetAllEnt()
        {
            try
            {
                //var consumeResult = consumer.Consume();
                //consumer.Commit(consumeResult);
                //Console.WriteLine(consumeResult.Value);

                //var message = new Message<Null, string> { Value = "GetAllEnt Success" };
                //producer.ProduceAsync("OutTopic", message).GetAwaiter().GetResult();


                return dbContext.Posts.Select(_mapper.Map<PostResponseTo>);

            }
            catch
            {
                throw new Exception("Getting all Messages exception");
            }
        }

        public async Task<IResponseTo> UpdateEnt(IRequestTo Dto)
        {
            //var consumeResult = consumer.Consume();
            //consumer.Commit(consumeResult);
            //Console.WriteLine(consumeResult.Value);

            //var message = new Message<Null, string> { Value = "UpdateEnt Success" };
            //producer.ProduceAsync("OutTopic", message).GetAwaiter().GetResult();

            var PostDto = (PostRequestTo)Dto;

            if (!Validate(PostDto))

            {
                throw new InvalidDataException("Incorrect data");

            }

            string json;
            json = $"{{\"Id\":{PostDto.Id},\"storyId\":{PostDto.storyId},\"content\":\"{PostDto.Content}\"}}";

            //
            redisDB.StringSet(PostDto.Id.ToString(), json);
            Console.WriteLine("Set: " + json);
            //

            var newMessage = _mapper.Map<Post>(PostDto);
            dbContext.Posts.Update(newMessage);
            await dbContext.SaveChangesAsync();
            var Message = _mapper.Map<PostResponseTo>(await dbContext.Posts.FindAsync(newMessage.Id));



            return Message;


        }

        public bool Validate(PostRequestTo MessageDto)
        {
            if (MessageDto?.Content?.Length < 2 || MessageDto?.Content?.Length > 2048)
                return false;

            return true;
        }
    }
}
