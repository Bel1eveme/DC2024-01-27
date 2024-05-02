using System.Reflection;
using Microsoft.EntityFrameworkCore;

namespace Publisher.Models.Entity;

public class AppDbContext : DbContext
{
    public DbSet<Label> Labels { get; set; }

    public DbSet<News> News { get; set; }

    public DbSet<User> Users { get; set; }
    
    public AppDbContext()
    {

    }
    public AppDbContext(DbContextOptions<AppDbContext> options)
        : base(options)
    {

    }
    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        //
    }
    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.ApplyConfigurationsFromAssembly(Assembly.GetExecutingAssembly());
        
    }
}