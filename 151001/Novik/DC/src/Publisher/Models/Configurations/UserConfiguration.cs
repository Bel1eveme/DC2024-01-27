using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Publisher.Models.Entity;

namespace REST.Models.Configurations;

public class UserConfiguration : IEntityTypeConfiguration<User>
{
    public void Configure(EntityTypeBuilder<User> entity)
    {
        entity.HasKey(e => e.id);
        entity.Property(e => e.id)
            .UseIdentityColumn()
            .IsRequired()
            .ValueGeneratedOnAdd();

        entity.HasIndex(e => e.login)
            .IsUnique();
        entity.Property(e => e.login)
            .HasMaxLength(64);

        entity.Property(e => e.password)
            .HasMaxLength(128);
        
        entity.Property(e => e.firstname)
            .HasMaxLength(64);
        
        entity.Property(e => e.lastname)
            .HasMaxLength(64);
    }
}