using Microsoft.EntityFrameworkCore;
using Publisher.Models.Entity;
using Publisher.Repositories.interfaces;

namespace Publisher.Repositories.Db;

public class UserRepository :  IUserRepository
{
    private readonly AppDbContext _context;

    public UserRepository(AppDbContext context)
    {
        _context = context;
    }
    public async Task<User?> GetByIdAsync(long id)
    {
        return await _context.Users.FirstOrDefaultAsync(label => label.id == id);
    }

    public async Task<IEnumerable<User>> GetAllAsync()
    {
        return await _context.Users.ToListAsync();
    }

    public async Task<User> AddAsync(User entity)
    {
        var entityEntry = await _context.Users.AddAsync(entity);
        await _context.SaveChangesAsync();
        return entityEntry.Entity;
    }

    public async Task<User> UpdateAsync(User entity)
    {
        _context.Users.Update(entity);
        await _context.SaveChangesAsync(); // Сохранить изменения в базе данных
        return entity;
    }

    public async Task<bool> Exists(long id)
    {
        return _context.Users.AsNoTracking().Any(label => label.id == id);
    }

    public async Task DeleteAsync(long id)
    {
        var entity = await _context.Users.FindAsync(id);
        if (entity != null)
        {
            _context.Users.Remove(entity);
            await _context.SaveChangesAsync();
        }
    }
}