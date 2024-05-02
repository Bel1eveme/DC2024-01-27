using Forum.Api.Models;
using Microsoft.EntityFrameworkCore;

namespace Forum.Api.Repositories;

public class CreatorRepository : ICreatorRepository
{
    private readonly AppDbContext _context;

    public CreatorRepository(AppDbContext context)
    {
        _context = context;
    }

    public async Task<List<Creator>> GetAllAsync()
    {
        return await _context.Creators.ToListAsync();
    }
    
    
    public async Task<Creator?> GetByIdAsync(long id)
    {
        var result = await _context.Creators.FindAsync(id);
        
        return result;
    }

    public async Task<Creator> CreateAsync(Creator creatorModel)
    {
        
        var result = await _context.Creators.AddAsync(creatorModel);
        
        await _context.SaveChangesAsync();
        
        return result.Entity;
    }
    

    public async Task<Creator?> UpdateAsync(long id, Creator updatedCreator)
    {
        var existingCreator = await _context.Creators.FirstOrDefaultAsync(x => x.Id == id);

        if (existingCreator == null)
        {
            return null;
        }

        existingCreator.FirstName = updatedCreator.FirstName;
        existingCreator.LastName = updatedCreator.LastName;
        existingCreator.Login = updatedCreator.Login;
        existingCreator.Password = updatedCreator.Password;

        await _context.SaveChangesAsync();
        
        return existingCreator;
    }

    public async Task<Creator?> DeleteAsync(long id)
    {
        var creatorModel = await _context.Creators.FirstOrDefaultAsync(x => x.Id == id);

        if (creatorModel == null)
        {
            return null;
        }

        _context.Creators.Remove(creatorModel);
        await _context.SaveChangesAsync();

        return creatorModel;
    }
}