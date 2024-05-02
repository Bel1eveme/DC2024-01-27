using TaskSQL.Dto.Request.CreateTo;
using TaskSQL.Dto.Request.UpdateTo;
using TaskSQL.Dto.Response;

namespace TaskSQL.Services.Interfaces;

public interface IPostService
{
    Task<PostResponseTo> GetPostById(long id);
    Task<IEnumerable<PostResponseTo>> GetPosts();
    Task<PostResponseTo> CreatePost(CreatePostRequestTo createPostRequestTo);
    Task DeletePost(long id);
    Task<PostResponseTo> UpdatePost(UpdatePostRequestTo modifiedPost);
}