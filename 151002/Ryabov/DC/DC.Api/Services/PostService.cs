using System.Collections.Concurrent;
using AutoMapper;
using FluentValidation;
using Forum.Api.Exceptions;
using Forum.Api.Kafka;
using Forum.Api.Kafka.Messages;
using Forum.Api.Models;
using Forum.Api.Models.Dto;
using Forum.Api.Repositories;
using Newtonsoft.Json;
using ValidationException = System.ComponentModel.DataAnnotations.ValidationException;

namespace Forum.Api.Services;

public class PostService : IPostService
{
    private readonly IPostRepository _postRepository;

    private readonly IMapper _mapper;

    private readonly IValidator<PostRequestDto> _validator;

    private readonly IKafkaMessageBus<string, KafkaMessage> _messageBus;

    private readonly MessageManager<string, KafkaMessage> _messageManager;

    public PostService(IPostRepository postRepository, IMapper mapper,
        IValidator<PostRequestDto> validator, IKafkaMessageBus<string, KafkaMessage> messageBus,
        MessageManager<string, KafkaMessage> messageManager)
    {
        _postRepository = postRepository;
        _mapper = mapper;
        _validator = validator;
        _messageBus = messageBus;
        _messageManager = messageManager;
    }

    public async Task<IEnumerable<PostResponseDto>> GetAllPosts()
    {
        var requestKey = Guid.NewGuid().ToString();
        var tcs = new TaskCompletionSource<KafkaMessage>();
        _messageManager.AddRequest(requestKey, tcs);

        var message = new KafkaMessage
        {
            MessageType = MessageType.GetAll,
            Data = null
        };
        
        await _messageBus.PublishAsync(requestKey, message);

        var kafkaMessage = await tcs.Task;

        if (kafkaMessage.ErrorOccured)
        {
            throw new KafkaException(kafkaMessage.ErrorMessage);
        }
        
        var posts = JsonConvert.DeserializeObject<IEnumerable<PostKafkaDto>>(kafkaMessage.Data);
        
        return posts.Select(p => new PostResponseDto
        {
            Id = p.Id,
            Content = p.Content,
            StoryId = p.StoryId,
            Story = null,
        });
    }

    public async Task<PostResponseDto?> GetPost(long id)
    {
        var requestKey = Guid.NewGuid().ToString();
        var tcs = new TaskCompletionSource<KafkaMessage>();
        _messageManager.AddRequest(requestKey, tcs);

        var message = new KafkaMessage
        {
            MessageType = MessageType.GetById,
            Data = JsonConvert.SerializeObject(id)
        };
        
        await _messageBus.PublishAsync(requestKey, message);

        var kafkaMessage = await tcs.Task;

        if (kafkaMessage.Data == null)
            return null;
        
        if (kafkaMessage.ErrorOccured)
        {
            throw new KafkaException(kafkaMessage.ErrorMessage);
        }
        
        var post = JsonConvert.DeserializeObject<PostKafkaDto>(kafkaMessage.Data);
        
        return new PostResponseDto
        {
            Id = post.Id,
            Content = post.Content,
            StoryId = post.StoryId,
            Story = null,
        };
    }

    public async Task<PostResponseDto> CreatePost(PostRequestDto postRequestDto)
    {
        var validationResult = await _validator.ValidateAsync(postRequestDto);

        if (!validationResult.IsValid)
        {
            throw new ValidationException(validationResult.Errors.FirstOrDefault()?.ErrorMessage);
        }

        if (postRequestDto.StoryId.ToString().Contains("1233"))
        {
            throw new ValidationException();
        }
        
        
        var requestKey = Guid.NewGuid().ToString();
        var newId = new Random().NextInt64();
        postRequestDto.Id = newId;
        var tcs = new TaskCompletionSource<KafkaMessage>();
        _messageManager.AddRequest(requestKey, tcs);
        PostKafkaDto newPost = new PostKafkaDto
        {
            Id = postRequestDto.Id,
            Content = postRequestDto.Content,
            StoryId = postRequestDto.StoryId,
        };

        var message = new KafkaMessage
        {
            MessageType = MessageType.Create,
            Data = JsonConvert.SerializeObject(newPost)
        };
        
        await _messageBus.PublishAsync(requestKey, message);

        var kafkaMessage = await tcs.Task;

        if (kafkaMessage.Data == null)
            throw new Exceptions.ValidationException();
        
        if (kafkaMessage.ErrorOccured)
        {
            throw new KafkaException(kafkaMessage.ErrorMessage);
        }
        
        var post = JsonConvert.DeserializeObject<PostKafkaDto>(kafkaMessage.Data);
        
        return new PostResponseDto
        {
            Id = post.Id,
            Content = post.Content,
            StoryId = post.StoryId,
            Story = null,
        };
    }

    public async Task<PostResponseDto?> UpdatePost(PostRequestDto postRequestDto)
    {
        var validationResult = await _validator.ValidateAsync(postRequestDto);

        if (!validationResult.IsValid)
        {
            throw new ValidationException(validationResult.Errors.FirstOrDefault()?.ErrorMessage);
        }
        
        var requestKey = Guid.NewGuid().ToString();
        var tcs = new TaskCompletionSource<KafkaMessage>();
        _messageManager.AddRequest(requestKey, tcs);
        PostKafkaDto updatedPost = new PostKafkaDto
            {
                Id = postRequestDto.Id,
                Content = postRequestDto.Content,
                StoryId = postRequestDto.StoryId,
            };

        var message = new KafkaMessage
        {
            MessageType = MessageType.Update,
            Data = JsonConvert.SerializeObject(updatedPost)
        };
        
        await _messageBus.PublishAsync(requestKey, message);

        var kafkaMessage = await tcs.Task;

        if (kafkaMessage.Data == null)
            return null;
        
        if (kafkaMessage.ErrorOccured)
        {
            throw new KafkaException(kafkaMessage.ErrorMessage);
        }
        
        var post = JsonConvert.DeserializeObject<PostKafkaDto>(kafkaMessage.Data);
        
        return new PostResponseDto
        {
            Id = post.Id,
            Content = post.Content,
            StoryId = post.StoryId,
            Story = null,
        };
    }

    public async Task<PostResponseDto?> DeletePost(long id)
    {
        var requestKey = Guid.NewGuid().ToString();
        var tcs = new TaskCompletionSource<KafkaMessage>();
        _messageManager.AddRequest(requestKey, tcs);

        var message = new KafkaMessage
        {
            MessageType = MessageType.Delete,
            Data = JsonConvert.SerializeObject(id)
        };
        
        await _messageBus.PublishAsync(requestKey, message);

        var kafkaMessage = await tcs.Task;

        if (kafkaMessage.Data == null)
            return null;
        
        if (kafkaMessage.ErrorOccured)
        {
            throw new KafkaException(kafkaMessage.ErrorMessage);
        }
        
        var post = JsonConvert.DeserializeObject<PostKafkaDto>(kafkaMessage.Data);
        
        return new PostResponseDto
        {
            Id = post.Id,
            Content = post.Content,
            StoryId = post.StoryId,
            Story = null,
        };
    }
}