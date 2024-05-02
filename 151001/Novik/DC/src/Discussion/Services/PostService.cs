using AutoMapper;
using Discussion.Models.DTOs.Requests;
using Discussion.Models.DTOs.Responses;
using Discussion.Models.Entity;
using Discussion.Repositories.interfaces;
using Discussion.Services.interfaces;
using KeyNotFoundException = System.Collections.Generic.KeyNotFoundException;

namespace Discussion.Services;

public class PostService : IPostService
{
    private readonly IPostRepository _postRepository;
    private readonly IMapper _postMapper;

    public PostService(IPostRepository postRepository,IMapper postMapper)
    {
        _postMapper = postMapper;
        _postRepository = postRepository;
    }
    
    public async Task<IEnumerable<PostResponseTo>> GetAllAsync()
    {
        var postEntities = await _postRepository.GetAllAsync();
        var res = _postMapper.Map<IEnumerable<PostResponseTo>>(postEntities);
        return res;
    }

    public async Task<PostResponseTo?> GetByIdAsync(long id)
    {
        var postEntity = await _postRepository.GetByIdAsync(id);
        /*if (postEntity == null)
        {
            throw new KeyNotFoundException($"User with ID {id} not found.");
        }*/
        return _postMapper.Map<PostResponseTo>(postEntity);
    }



    public async Task<PostResponseTo> AddAsync(PostRequestTo postRequest, string country)
    {
        var postEntity = _postMapper.Map<Post>(postRequest);
        postEntity.country = country;
        postEntity = await _postRepository.AddAsync(postEntity);
        return _postMapper.Map<PostResponseTo>(postEntity);
    }
    

    public async Task<PostResponseTo> UpdateAsync(PostRequestTo postRequest, string country)
    {
        var existingpost = await _postRepository.Exists(postRequest.id);
        /*if (!existingpost)
        {
            throw new KeyNotFoundException($"User with ID {postRequest.id} not found.");
        }*/

        var updatedpost = _postMapper.Map<Post>(postRequest);
        updatedpost.country = country;
        return _postMapper.Map<PostResponseTo>(await _postRepository.UpdateAsync(updatedpost));
    }

    public async Task DeleteAsync(long id)
    {
        var existingpost = await _postRepository.Exists(id);
        /*if (!existingpost)
        {
            throw new KeyNotFoundException($"User with ID {id} not found.");
        }*/
        await _postRepository.DeleteAsync(id);
    }

    
}