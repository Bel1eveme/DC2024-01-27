using Publisher.Clients.Discussion.Dto.Request;
using Publisher.Clients.Discussion.Dto.Response;
using Refit;
namespace Publisher.Clients.Discussion;

public interface IDiscussionService
{
    [Get("/posts/{id}")]
    Task<DiscussionPostResponseTo> GetPost(long id);

    [Get("/posts")]
    Task<IEnumerable<DiscussionPostResponseTo>> GetPosts();

    [Post("/posts")]
    Task<DiscussionPostResponseTo> CreatePost([Body] DiscussionPostRequestTo createPostRequestTo);

    [Delete("/posts/{id}")]
    Task DeletePost(long id);

    [Put("/posts")]
    Task<DiscussionPostResponseTo> UpdatePost([Body] DiscussionPostRequestTo postRequestTo);
}