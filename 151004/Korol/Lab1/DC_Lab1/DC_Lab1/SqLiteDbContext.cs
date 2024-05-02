using System;
using System.Collections.Generic;
using DC_Lab1.Models;
using Microsoft.EntityFrameworkCore;

namespace DC_Lab1;

public partial class SqLiteDbContext : DbContext
{
    /*
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

    public virtual DbSet<Message> Messages { get; set; }

    public virtual DbSet<Sticker> Stickers { get; set; }

    public virtual DbSet<Story> Storys { get; set; }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        => optionsBuilder.UseSqlite("DataSource=file::memory:?cache=shared");
    */
}
