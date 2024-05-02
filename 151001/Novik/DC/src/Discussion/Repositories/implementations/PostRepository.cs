using Cassandra.Mapping;
using Discussion.Models.Entity;
using Discussion.Repositories.interfaces;

namespace Discussion.Repositories.implementations;

public class PostRepository :  IPostRepository
{
    private readonly IMapper _mapper;

    public PostRepository(IMapper mapper)
    {
        _mapper = mapper;
    }

    public async Task<IEnumerable<Post>> GetAllAsync()
    {
        return await _mapper.FetchAsync<Post>();
    }

    public async Task<Post?> GetByIdAsync(long id)
    {
        return await _mapper.FirstOrDefaultAsync<Post>("SELECT * FROM post WHERE id = ? ALLOW FILTERING",  id);
    }

    public async Task<Post> AddAsync(Post post)
    {
        DateTime currentTime = DateTime.Now;
        long id = long.Parse(currentTime.ToString("yyyyMMddHHmmss"));
        post.id = id;
        await _mapper.InsertAsync(post);
        return post;
    }

    public async Task<Post> UpdateAsync(Post post)
    {
        await _mapper.UpdateAsync<Post>("SET content = ? WHERE id = ? AND country = ? AND newsid = ?", post.content, post.id, post.country, post.newsId);

        return post;
    }

    public async Task<bool> Exists(long id)
    {
        var post = await _mapper.SingleOrDefaultAsync<Post>("SELECT * FROM post WHERE id = ? ALLOW FILTERING", id);
        return post != null;
    }

    public async Task DeleteAsync(long id)
    {
        IEnumerable<Post> postsToDelete = await _mapper.FetchAsync<Post>("SELECT * FROM post WHERE id = ? ALLOW FILTERING",id);

// Удаляем каждую запись по отдельности
        foreach (Post post in postsToDelete)
        {
            await _mapper.DeleteAsync<Post>("WHERE id = ? AND country = ? AND newsid = ?", post.id,post.country,post.newsId);
        }
    }
}