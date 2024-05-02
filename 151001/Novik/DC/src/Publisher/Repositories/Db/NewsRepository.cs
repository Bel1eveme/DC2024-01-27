using Microsoft.EntityFrameworkCore;
using Publisher.Models.Entity;
using Publisher.Repositories.interfaces;

namespace Publisher.Repositories.Db;

public class NewsRepository :  INewsRepository
{
    private readonly AppDbContext _context;

    public NewsRepository(AppDbContext context)
    {
        _context = context;
    }
    public async Task<News?> GetByIdAsync(long id)
    {
        return await _context.News.FirstOrDefaultAsync(label => label.id == id);
    }
    public async Task<IEnumerable<News>> GetAllAsync()
    {
        return await _context.News.ToListAsync();
    }

    public async Task<News> AddAsync(News entity)
    {
        var entityEntry = await _context.News.AddAsync(entity);
        await _context.SaveChangesAsync();
        return entityEntry.Entity;
    }

    public async Task<News> UpdateAsync(News entity)
    {
        var entityEntry = _context.News.Update(entity);
        await _context.SaveChangesAsync(); // Сохранить изменения в базе данных
        return entityEntry.Entity;
    }

    public async Task<bool> Exists(long id)
    {
        return _context.News.AsNoTracking().Any(label => label.id == id);
    }

    public async Task DeleteAsync(long id)
    {
        var entity = await _context.News.FindAsync(id);
        if (entity != null)
        {
            _context.News.Remove(entity);
            await _context.SaveChangesAsync();
        }
    }
}