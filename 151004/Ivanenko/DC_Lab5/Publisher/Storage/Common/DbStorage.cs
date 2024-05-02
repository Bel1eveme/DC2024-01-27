using Microsoft.EntityFrameworkCore;
using Publisher.Entity.Db;

namespace Publisher.Storage.Common
{
    public abstract class DbStorage : DbContext
    {
        public DbSet<Editor> Authors { get; set; }
        public DbSet<Sticker> Markers { get; set; }
        public DbSet<Tweet> Tweets { get; set; }

        public DbStorage()
        {
            Database.EnsureCreated();
            ChangeTracker.AutoDetectChangesEnabled = true;
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<Editor>(entity =>
            {
                entity.ToTable("tbl_authors");

                entity.HasKey(e => e.Id);

                entity.Property(a => a.Id).ValueGeneratedOnAdd();
                entity.Property(a => a.Password).IsRequired().HasMaxLength(128);
                entity.Property(a => a.FirstName).IsRequired().HasMaxLength(64).IsUnicode();
                entity.Property(a => a.LastName).IsRequired().HasMaxLength(64).IsUnicode();
                entity.Property(a => a.Login).IsRequired().HasMaxLength(64).IsUnicode();

                entity.HasIndex(a => a.Login).IsUnique();

                entity.HasMany(a => a.Tweets).WithOne(t => t.Editor).HasForeignKey(t => t.EditorId);
            });

            modelBuilder.Entity<Tweet>(entity =>
            {
                entity.ToTable("tbl_tweets");

                entity.HasKey(t => t.Id);

                entity.Property(a => a.Id).ValueGeneratedOnAdd();
                entity.Property(t => t.Content).IsRequired().HasMaxLength(2048);
                entity.Property(t => t.Created).IsRequired();
                entity.Property(t => t.Modified).IsRequired();
                entity.Property(t => t.Title).IsRequired().HasMaxLength(64);
                entity.Property(t => t.EditorId).IsRequired();

                entity.HasIndex(t => t.Title).IsUnique();
            });

            modelBuilder.Entity<Sticker>(entity =>
            {
                entity.ToTable("tbl_markers");

                entity.HasKey(m => m.Id);
                entity.Property(m => m.Id).ValueGeneratedOnAdd();
                entity.Property(m => m.Name).IsRequired().HasMaxLength(32);

                entity.HasIndex(m => m.Name).IsUnique();

                entity.HasMany(m => m.Tweets).WithMany(t => t.Stickers);
            });
        }
    }
}
