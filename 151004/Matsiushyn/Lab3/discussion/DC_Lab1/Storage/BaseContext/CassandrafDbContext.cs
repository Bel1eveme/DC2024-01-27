using DC_Lab1.DB.BaseDBContext;
using Microsoft.EntityFrameworkCore;
using Cassandra;

namespace Lab2.Storage.BaseContext
{
    public class CassandrafDbContext : DbContext
    {
        public CassandrafDbContext()
        { }

        public CassandrafDbContext(DbContextOptions<CassandrafDbContext> options)
            : base(options)
        { }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            //optionsBuilder.UseCassandra("Host=localhost;Port=9042;Database=distcomp");
        }
    }
}