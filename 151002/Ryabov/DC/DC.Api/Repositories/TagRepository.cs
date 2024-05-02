using Forum.Api.Models;
using Microsoft.EntityFrameworkCore;

namespace Forum.Api.Repositories;

public class TagRepository : ITagRepository
{
    private readonly AppDbContext _context;

    public TagRepository(AppDbContext context)
    {
        _context = context;
    }
    
    public async Task<List<Tag>> GetAllAsync()
    {
        return await _context.Tags.ToListAsync();
    }

    public async Task<Tag?> GetByIdAsync(long id)
    {
        return await _context.Tags.FindAsync(id);
    }

    public async Task<Tag> CreateAsync(Tag tagModel)
    {
        await _context.Tags.AddAsync(tagModel);
        await _context.SaveChangesAsync();

        return tagModel;
    }
    

    public async Task<Tag?> UpdateAsync(long id, Tag updatedTag)
    {
        var existingTag = await _context.Tags.FirstOrDefaultAsync(x => x.Id == id);

        if (existingTag == null)
        {
            return null;
        }

        existingTag.Name = updatedTag.Name;
        existingTag.Stories = updatedTag.Stories;
        
        await _context.SaveChangesAsync();
        
        return existingTag;
    }

    public async Task<Tag?> DeleteAsync(long id)
    {
        var tagModel = await _context.Tags.FirstOrDefaultAsync(x => x.Id == id);

        if (tagModel == null)
        {
            return null;
        }

        _context.Tags.Remove(tagModel);
        await _context.SaveChangesAsync();

        return tagModel;
    }
}