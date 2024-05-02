using Microsoft.EntityFrameworkCore;
using LR2.Models;

namespace LR2.Storage;

public class AppDbContext(DbContextOptions<AppDbContext> options) : DbContext(options)
{
    public DbSet<Creator> Creators { get; set; } = null!;
    public DbSet<Label> Labels { get; set; } = null!;
    public DbSet<Issue> Issues { get; set; } = null!;
    public DbSet<Comment> Comments { get; set; } = null!;
}