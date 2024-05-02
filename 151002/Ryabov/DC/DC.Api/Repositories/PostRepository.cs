using Forum.Api.Models;
using Microsoft.EntityFrameworkCore;

namespace Forum.Api.Repositories;

public class PostRepository : IPostRepository
{
    private readonly AppDbContext _context;

    public PostRepository(AppDbContext context)
    {
        _context = context;
    }

    public async Task<List<Post>> GetAllAsync()
    {
        return await _context.Posts.ToListAsync();
    }

    public async Task<Post?> GetByIdAsync(long id)
    {
        return await _context.Posts.FindAsync(id);
    }

    public async Task<Post> CreateAsync(Post postModel)
    {
        await _context.Posts.AddAsync(postModel);
        await _context.SaveChangesAsync();

        return postModel;
    }
    

    public async Task<Post?> UpdateAsync(long id, Post updatedPost)
    {
        var existingPost = await _context.Posts.FirstOrDefaultAsync(x => x.Id == id);

        if (existingPost == null)
        {
            return null;
        }

        existingPost.Content = updatedPost.Content;
        existingPost.Story = updatedPost.Story;

        await _context.SaveChangesAsync();
        
        return existingPost;
    }

    public async Task<Post?> DeleteAsync(long id)
    {
        var postModel = await _context.Posts.FirstOrDefaultAsync(x => x.Id == id);

        if (postModel == null)
        {
            return null;
        }

        _context.Posts.Remove(postModel);
        await _context.SaveChangesAsync();

        return postModel;
    }
}