using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Publisher.Models.Entity;

namespace REST.Models.Configurations;

public class NewsConfiguration : IEntityTypeConfiguration<News>
{
    public void Configure(EntityTypeBuilder<News> entity)
    {
        entity.HasKey(e => e.id);
        entity.Property(e => e.id)
            .UseIdentityColumn()
            .IsRequired()
            .ValueGeneratedOnAdd();

        entity.HasIndex(e => e.userId);
        
        entity.HasIndex(e => e.title)
            .IsUnique();
        entity.Property(e => e.title)
            .HasMaxLength(64);

        entity.Property(e => e.content)
            .HasMaxLength(2048);
        
        entity
            .HasMany(c => c.Labels)
            .WithMany(s => s.News)
            .UsingEntity<NewsLabel>(
                j => j
                    .HasOne(pt => pt.Label)
                    .WithMany(t => t.NewsLabels)
                    .HasForeignKey(pt => pt.labelId),
                j => j
                    .HasOne(pt => pt.News)
                    .WithMany(p => p.NewsLabels)
                    .HasForeignKey(pt => pt.newsId),
                j =>
                {
                    j.ToTable("NewsLabel");
                });
        
        entity
            .HasOne(n => n.User)
            .WithMany(u => u.News)
            .HasForeignKey(n => n.userId);
    }
}