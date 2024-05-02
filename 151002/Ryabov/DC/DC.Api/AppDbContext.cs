using Forum.Api.Models;
using Microsoft.EntityFrameworkCore;

namespace Forum.Api;

public sealed class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options)
    {
        
        
        //ApplyMigrations(this);
    }

    private void ApplyMigrations(AppDbContext context)
    {
        /*if (context.Database.GetPendingMigrations().Any())
        {
            context.Database.MigrateAsync();
        }*/
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.Entity<Creator>().HasIndex(c => c.Login).IsUnique();
        modelBuilder.Entity<Story>().HasIndex(s => s.Title).IsUnique();
        modelBuilder.Entity<Tag>();
        modelBuilder.Entity<Post>();
    }
    
    public DbSet<Creator> Creators { get; init; }

    public DbSet<Tag> Tags { get; init; }
    
    public DbSet<Story> Stories { get; init; }
    
    public DbSet<Post> Posts { get; init; }
}