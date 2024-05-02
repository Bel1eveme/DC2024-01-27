using Forum.Api.Models.Dto;
using Forum.PostApi.Kafka.Messages;
using Forum.PostApi.Models;
using Forum.PostApi.Models.Dto;
using Forum.PostApi.Services;
using Newtonsoft.Json;

namespace Forum.PostApi.Kafka.Consumer;

public class PostKafkaHandler : IKafkaHandler<string, KafkaMessage>
{
    private readonly IKafkaMessageBus<string, KafkaMessage> _kafkaMessageBus;

    private readonly IPostService _postService;

    public PostKafkaHandler(IKafkaMessageBus<string, KafkaMessage> kafkaMessageBus, IPostService postService)
    {
        _kafkaMessageBus = kafkaMessageBus;
        _postService = postService;
    }

    public async Task HandleAsync(string key, KafkaMessage value)
    {
        //await Console.Out.WriteLineAsync(JsonConvert.SerializeObject(JsonConvert.DeserializeObject<Post>(value.Data)));
        KafkaMessage message;

        switch (value.MessageType)
        {
            case MessageType.GetAll:
            {
                try
                {
                    var posts = await _postService.GetAllPostsAsync();

                    message = new KafkaMessage
                    {
                        MessageType = MessageType.GetAll,
                        Data = JsonConvert.SerializeObject(posts.Select(p => new PostKafkaDto
                        {
                            StoryId = p.StoryId,
                            Id = p.Id,
                            Content = p.Content
                        }))
                    };
                }
                catch (Exception e)
                {
                    message = new KafkaMessage
                    {
                        MessageType = MessageType.GetAll,
                        Data = null,
                        ErrorOccured = true,
                        ErrorMessage = e.Message,
                    };
                }

                break;
            }
            case MessageType.GetById:
            {
                try
                {
                    var id = JsonConvert.DeserializeObject<long>(value.Data);

                    var post = await _postService.GetPostAsync(id);

                    message = new KafkaMessage
                    {
                        MessageType = MessageType.GetById,
                        Data = JsonConvert.SerializeObject(post)
                    };
                }
                catch (Exception e)
                {
                    message = new KafkaMessage
                    {
                        MessageType = MessageType.GetById,
                        Data = null,
                        ErrorOccured = true,
                        ErrorMessage = e.Message,
                    };
                }
                
                break;
            }
            case MessageType.Create:
                try
                {
                    var postToCreate = JsonConvert.DeserializeObject<PostKafkaDto>(value.Data);

                    var newPost = new PostRequestDto
                    {
                        Country = "ru",
                        Id = postToCreate.Id,
                        StoryId = postToCreate.StoryId,
                        Content = postToCreate.Content,
                    };
                    
                    var post = await _postService.CreatePostAsync(newPost);
                    
                    message = new KafkaMessage
                    {
                        MessageType = MessageType.Create,
                        Data = post is null ? null : JsonConvert.SerializeObject(new PostKafkaDto()
                        {
                            Id = post.Id,
                            StoryId = post.StoryId,
                            Content = post.Content
                        }),
                    };
                }
                catch (Exception e)
                {
                    message = new KafkaMessage
                    {
                        MessageType = MessageType.Create,
                        Data = null,
                        ErrorOccured = true,
                        ErrorMessage = e.Message,
                    };
                }
                
                break;
            case MessageType.Update:
                try
                {
                    var postToUpdate = JsonConvert.DeserializeObject<PostKafkaDto>(value.Data);

                    var post = await _postService.UpdatePostAsync(new PostRequestDto
                    {
                        Id = postToUpdate.Id,
                        Country = postToUpdate.Country,
                        StoryId = postToUpdate.StoryId,
                        Content = postToUpdate.Content
                    });

                    message = new KafkaMessage
                    {
                        MessageType = MessageType.Create,
                        Data = post is null ? null : JsonConvert.SerializeObject(new PostKafkaDto
                        {
                            Id = post.Id,
                            StoryId = post.StoryId,
                            Content = post.Content
                        }),
                    };
                }
                catch (Exception e)
                {
                    message = new KafkaMessage
                    {
                        MessageType = MessageType.Update,
                        Data = null,
                        ErrorOccured = true,
                        ErrorMessage = e.Message,
                    };
                }
                
                break;
            case MessageType.Delete:
                try
                {
                    var postToFind = JsonConvert.DeserializeObject<long>(value.Data);

                    var post = await _postService.DeletePostAsync(postToFind);

                    message = new KafkaMessage
                    {
                        MessageType = MessageType.Delete,
                        Data = post is null ? null : JsonConvert.SerializeObject(new PostKafkaDto
                        {
                            Id = post.Id,
                            StoryId = post.StoryId,
                            Content = post.Content
                        }),
                    };
                }
                catch (Exception e)
                {
                    message = new KafkaMessage
                    {
                        MessageType = MessageType.Delete,
                        Data = null,
                        ErrorOccured = true,
                        ErrorMessage = e.Message,
                    };
                }
                
                break;
            default:
                throw new ArgumentOutOfRangeException();
        }

        await _kafkaMessageBus.PublishAsync(key, message);
    }
}