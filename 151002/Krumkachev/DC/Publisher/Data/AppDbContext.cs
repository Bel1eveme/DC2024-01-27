using Microsoft.EntityFrameworkCore;
using Publisher.Models;

namespace Publisher.Data;

public class AppDbContext : DbContext
{
	public AppDbContext(DbContextOptions<AppDbContext> options) : base(options)
	{
	}

	public DbSet<Creator> Creators { get; set; }
	public DbSet<Issue> Issue { get; set; }
	public DbSet<Label> Labels { get; set; }

	protected override void OnModelCreating(ModelBuilder modelBuilder)
	{
		base.OnModelCreating(modelBuilder);

		modelBuilder.Entity<Issue>()
			.HasMany(n => n.Labels)
			.WithMany(s => s.Issue)
			.UsingEntity<IssueLabel>(
				ns => ns.HasOne<Label>().WithMany().HasForeignKey("LabelId"),
				ns => ns.HasOne<Issue>().WithMany().HasForeignKey("IssueId"),
				ns =>
				{
					ns.ToTable("tbl_Issue_Labels");
					ns.HasKey("IssueId", "LabelId");
				});

		modelBuilder.Entity<Creator>()
			.HasIndex(c => c.Login)
			.IsUnique();

		modelBuilder.Entity<Issue>()
			.HasIndex(c => c.Title)
			.IsUnique();

		modelBuilder.Entity<Label>()
			.HasIndex(c => c.Name)
			.IsUnique();
	}
}