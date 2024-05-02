using System;
using System.Collections.Generic;
using DC_Lab1.Models;
using Microsoft.EntityFrameworkCore;

namespace DC_Lab1;

public partial class SqLiteDbContext : DbContext
{
    public SqLiteDbContext()
    {
        Database.EnsureCreated();
    }

    public SqLiteDbContext(DbContextOptions<SqLiteDbContext> options)
        : base(options)
    {
        Database.EnsureCreated();
    }

    public virtual DbSet<Author> Authors { get; set; }

    public virtual DbSet<Post> Messages { get; set; }

    public virtual DbSet<Label> Stickers { get; set; }

    public virtual DbSet<Story> Storys { get; set; }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        => optionsBuilder.UseSqlite("DataSource=file::memory:?cache=shared");

    //protected override void OnModelCreating(ModelBuilder modelBuilder)
    //{
    //    modelBuilder.Entity<Editor>(entity =>
    //    {
    //        entity.ToTable("Editor");

    //        entity.HasIndex(e => e.Login, "IX_Editor_login").IsUnique();

    //        entity.Property(e => e.Id)
    //            .ValueGeneratedNever()
    //            .HasColumnName("id");
    //        entity.Property(e => e.Firstname).HasColumnName("firstname");
    //        entity.Property(e => e.Lastname).HasColumnName("lastname");
    //        entity.Property(e => e.Login).HasColumnName("login");
    //        entity.Property(e => e.Password).HasColumnName("password");
    //    });

    //    modelBuilder.Entity<Post>(entity =>
    //    {
    //        entity.ToTable("Post");

    //        entity.Property(e => e.Id)
    //            .ValueGeneratedNever()
    //            .HasColumnName("id");
    //        entity.Property(e => e.Content).HasColumnName("content");
    //        entity.Property(e => e.storyId).HasColumnName("storyId");

    //        entity.HasOne(d => d.Story).WithMany(p => p.Posts).HasForeignKey(d => d.storyId);
    //    });

    //    modelBuilder.Entity<Sticker>(entity =>
    //    {
    //        entity.ToTable("Sticker");

    //        entity.HasIndex(e => e.Id, "IX_Sticker_id").IsUnique();

    //        entity.HasIndex(e => e.Name, "IX_Sticker_name").IsUnique();

    //        entity.Property(e => e.Id)
    //            .ValueGeneratedNever()
    //            .HasColumnName("id");
    //        entity.Property(e => e.Name).HasColumnName("name");
    //    });

    //    modelBuilder.Entity<Story>(entity =>
    //    {
    //        entity.ToTable("Story");

    //        entity.HasIndex(e => e.Title, "IX_Story_title").IsUnique();

    //        entity.Property(e => e.Id)
    //            .ValueGeneratedNever()
    //            .HasColumnName("id");
    //        entity.Property(e => e.Content).HasColumnName("content");
    //        entity.Property(e => e.Created).HasColumnName("created");
    //        entity.Property(e => e.Modified).HasColumnName("modified");
    //        entity.Property(e => e.Title).HasColumnName("title");

    //        entity.HasOne(d => d.Editor).WithMany(p => p.Storys).HasForeignKey(d => d.EditorId);
    //    });

    //    OnModelCreatingPartial(modelBuilder);
    //}

    //partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
