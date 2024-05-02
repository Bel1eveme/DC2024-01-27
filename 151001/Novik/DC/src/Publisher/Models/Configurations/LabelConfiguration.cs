using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using Publisher.Models.Entity;

namespace Publisher.Models.Configurations;

public class LabelConfiguration : IEntityTypeConfiguration<Label>
{
    public void Configure(EntityTypeBuilder<Label> entity)
    {
        entity.HasKey(e => e.id);
        entity.Property(e => e.id)
            .UseIdentityColumn()
            .IsRequired()
            .ValueGeneratedOnAdd();

        entity.HasIndex(e => e.name)
            .IsUnique();

        entity.Property(e => e.name)
            .HasMaxLength(32);
    }
}