using DC_Lab1.Models;
using Microsoft.EntityFrameworkCore;

namespace DC_Lab1.DB.BaseDBContext
{
    public partial class BaseContext : DbContext
    {
        public BaseContext()
        {

            Database.EnsureCreated();
        }

        public BaseContext(DbContextOptions<BaseContext> options)
            : base(options)
        {
            Database.EnsureCreated();
        }


        public virtual DbSet<Author> Authors { get; set; }

        public virtual DbSet<Post> Messages { get; set; }

        public virtual DbSet<Label> Stickers { get; set; }

        public virtual DbSet<Story> Storys { get; set; }

        public virtual DbSet<StoryLabel> StoryStickers { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Author>(entity =>
            {
                entity.ToTable("tbl_Author");
                entity.HasIndex(e => e.Login, "IX_Authors_login").IsUnique();

                entity.Property(e => e.Id)
                    .ValueGeneratedOnAdd()
                    .HasColumnName("id");
                entity.Property(e => e.Firstname).HasColumnName("firstname");
                entity.Property(e => e.Lastname).HasColumnName("lastname");
                entity.Property(e => e.Login).HasColumnName("login");
                entity.Property(e => e.Password).HasColumnName("password");
            });

            modelBuilder.Entity<Post>(entity =>
            {
                entity.ToTable("tbl_Message");
                entity.Property(e => e.Id)
                    .ValueGeneratedOnAdd()
                    .HasColumnName("id");
                entity.Property(e => e.Content).HasColumnName("content");
                entity.Property(e => e.storyId).HasColumnName("storyId");

                entity.HasOne(d => d.Story).WithMany(p => p.Messages).HasForeignKey(d => d.storyId);
            });

            modelBuilder.Entity<Label>(entity =>
            {
                entity.ToTable("tbl_Sticker");
                entity.HasIndex(e => e.Id, "IX_Stickers_id").IsUnique();

                entity.HasIndex(e => e.name, "IX_Stickers_name").IsUnique();

                entity.Property(e => e.Id)
                    .ValueGeneratedOnAdd()
                    .HasColumnName("id");
                entity.Property(e => e.name).HasColumnName("name");
            });

            modelBuilder.Entity<Story>(entity =>
            {
                entity.ToTable("tbl_Story");
                entity.HasIndex(e => e.Title, "IX_Storys_title").IsUnique();

                entity.Property(e => e.Id)
                    .ValueGeneratedOnAdd()
                    .HasColumnName("id");
                entity.Property(e => e.Content).HasColumnName("content");
                entity.Property(e => e.Created).HasColumnName("created");
                entity.Property(e => e.Modified).HasColumnName("modified");
                entity.Property(e => e.Title).HasColumnName("title");

                entity.HasOne(d => d.Author).WithMany(p => p.Storys).HasForeignKey(d => d.authorId);
            });

            modelBuilder.Entity<StoryLabel>(entity =>
            {
                entity.ToTable("tbl_StorySticker");

                entity.Property(e => e.Id)
                    .ValueGeneratedOnAdd()
                    .HasColumnName("id");
                entity.Property(e => e.stickerId).HasColumnName("stickerId");
                entity.Property(e => e.storyId).HasColumnName("storyId");

                entity.HasOne(d => d.Sticker).WithMany(p => p.StoryStickers).HasForeignKey(d => d.stickerId);

                entity.HasOne(d => d.Story).WithMany(p => p.StoryStickers).HasForeignKey(d => d.storyId);
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
