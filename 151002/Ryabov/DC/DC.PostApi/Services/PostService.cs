using FluentValidation;
using Forum.PostApi.Extensions;
using Forum.PostApi.Models;
using Forum.PostApi.Models.Dto;
using Forum.PostApi.Repositories;
using ValidationException = System.ComponentModel.DataAnnotations.ValidationException;

namespace Forum.PostApi.Services;

public class PostService : IPostService
{
    private readonly IPostRepository _postRepository; 

    private readonly IValidator<PostRequestDto> _validator;
    
    private readonly CassandraOptions _cassandraOptions;

    public PostService(IPostRepository postRepository, IValidator<PostRequestDto> validator, CassandraOptions cassandraOptions)
    {
        _postRepository = postRepository;
        _validator = validator;
        _cassandraOptions = cassandraOptions;
    }

    public async Task<IEnumerable<PostResponseDto>> GetAllPostsAsync()
    {
        return (await _postRepository.GetAllAsync()).Select(MapperHelper.PostToDto);
    }

    public async Task<PostResponseDto?> GetPostAsync(long id)
    {
        var post = await _postRepository.GetByIdAsync(id);
        
        return post is not null ? MapperHelper.PostToDto(post) : null;
    }

    public async Task<PostResponseDto?> CreatePostAsync(PostRequestDto postRequestDto)
    {
        var validationResult = await _validator.ValidateAsync(postRequestDto);

        if (!validationResult.IsValid)
        {
            throw new ValidationException(validationResult.Errors.FirstOrDefault()?.ErrorMessage);
        }
        
        postRequestDto.Country ??= _cassandraOptions.DefaultPartitionKey;
        
        var postModel = MapperHelper.DtoToPost(postRequestDto);
        
        var post = await _postRepository.AddAsync(postModel);

        return post is not null ? MapperHelper.PostToDto(post) : null;
    }

    public async Task<PostResponseDto?> UpdatePostAsync(PostRequestDto postRequestDto)
    {
        postRequestDto.Country ??= _cassandraOptions.DefaultPartitionKey;
        
        var postModel = MapperHelper.DtoToPost(postRequestDto);
        
        var post = await _postRepository.UpdateAsync(postModel);

        return post is not null ? MapperHelper.PostToDto(post) : null;
    }

    public async Task<PostResponseDto?> DeletePostAsync(long id)
    {
        var post = await _postRepository.DeleteAsync(id);

        return post is not null ? MapperHelper.PostToDto(post) : null;
    }
}