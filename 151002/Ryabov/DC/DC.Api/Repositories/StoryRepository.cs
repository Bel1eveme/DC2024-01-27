using Forum.Api.Models;
using Microsoft.EntityFrameworkCore;

namespace Forum.Api.Repositories;

public class StoryRepository : IStoryRepository
{
    private readonly AppDbContext _context;

    public StoryRepository(AppDbContext context)
    {
        _context = context;
    }
    
    public async Task<List<Story>> GetAllAsync()
    {
        return await _context.Stories.ToListAsync();
    }

    public IQueryable<Story> GetAllWithFilteringAsync()
    {
        return _context.Stories.AsQueryable();
    }

    public async Task<Story?> GetByIdAsync(long id)
    {
        return await _context.Stories.FindAsync(id);
    }

    public async Task<Story> CreateAsync(Story storyModel)
    {
        await _context.Stories.AddAsync(storyModel);
        await _context.SaveChangesAsync();

        return storyModel;
    }
    

    public async Task<Story?> UpdateAsync(long id, Story updatedStory)
    {
        var existingStory = await _context.Stories.FirstOrDefaultAsync(x => x.Id == id);

        if (existingStory == null)
        {
            return null;
        }

        existingStory.Title = updatedStory.Title;
        existingStory.Content = updatedStory.Content;
        existingStory.Created = updatedStory.Created;
        existingStory.Modified = updatedStory.Modified;
        existingStory.CreatorId = updatedStory.CreatorId;
        existingStory.Tags = updatedStory.Tags;

        await _context.SaveChangesAsync();
        
        return existingStory;
    }

    public async Task<Story?> DeleteAsync(long id)
    {
        var storyModel = await _context.Stories.FirstOrDefaultAsync(x => x.Id == id);

        if (storyModel == null)
        {
            return null;
        }

        _context.Stories.Remove(storyModel);
        await _context.SaveChangesAsync();

        return storyModel;
    }
}