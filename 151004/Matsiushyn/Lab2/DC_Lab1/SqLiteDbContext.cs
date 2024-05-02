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

    public virtual DbSet<Post> Posts { get; set; }

    public virtual DbSet<Label> Labels { get; set; }

    public virtual DbSet<Tweet> Tweets { get; set; }

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
    //        entity.Property(e => e.tweetId).HasColumnName("tweetId");

    //        entity.HasOne(d => d.Tweet).WithMany(p => p.Posts).HasForeignKey(d => d.tweetId);
    //    });

    //    modelBuilder.Entity<Label>(entity =>
    //    {
    //        entity.ToTable("Label");

    //        entity.HasIndex(e => e.Id, "IX_Label_id").IsUnique();

    //        entity.HasIndex(e => e.Name, "IX_Label_name").IsUnique();

    //        entity.Property(e => e.Id)
    //            .ValueGeneratedNever()
    //            .HasColumnName("id");
    //        entity.Property(e => e.Name).HasColumnName("name");
    //    });

    //    modelBuilder.Entity<Tweet>(entity =>
    //    {
    //        entity.ToTable("Tweet");

    //        entity.HasIndex(e => e.Title, "IX_Tweet_title").IsUnique();

    //        entity.Property(e => e.Id)
    //            .ValueGeneratedNever()
    //            .HasColumnName("id");
    //        entity.Property(e => e.Content).HasColumnName("content");
    //        entity.Property(e => e.Created).HasColumnName("created");
    //        entity.Property(e => e.Modified).HasColumnName("modified");
    //        entity.Property(e => e.Title).HasColumnName("title");

    //        entity.HasOne(d => d.Editor).WithMany(p => p.Tweets).HasForeignKey(d => d.EditorId);
    //    });

    //    OnModelCreatingPartial(modelBuilder);
    //}

    //partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
