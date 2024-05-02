using Microsoft.EntityFrameworkCore;
using Publisher.Models.Entity;
using Publisher.Repositories.interfaces;

namespace Publisher.Repositories.Db;

public class LabelRepository : ILabelRepository
{
    private readonly AppDbContext _context;

    public LabelRepository(AppDbContext context)
    {
        _context = context;
    }
    public async Task<Label?> GetByIdAsync(long id)
    {
        return await _context.Labels.FirstOrDefaultAsync(label => label.id == id);
    }

    public async Task<IEnumerable<Label>> GetAllAsync()
    {
        return await _context.Labels.ToListAsync();
    }

    public async Task<Label> AddAsync(Label entity)
    {
        var entityEntry = await _context.Labels.AddAsync(entity);
        await _context.SaveChangesAsync();
        return entityEntry.Entity;
    }

    public async Task<Label> UpdateAsync(Label entity)
    {
        var entityEntry = _context.Labels.Update(entity);
        await _context.SaveChangesAsync(); // Сохранить изменения в базе данных
        return entityEntry.Entity;
    }

    public async Task<bool> Exists(long id)
    {
        return _context.Labels.AsNoTracking().Any(label => label.id == id);
    }

    public async Task DeleteAsync(long id)
    {
        var entity = await _context.Labels.FindAsync(id);
        if (entity != null)
        {
            _context.Labels.Remove(entity);
            await _context.SaveChangesAsync();
        }
    }
}