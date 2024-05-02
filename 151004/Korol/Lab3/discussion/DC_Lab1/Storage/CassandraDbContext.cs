using DC_Lab1.DB.BaseDBContext;
using Microsoft.EntityFrameworkCore;
using Cassandra;

namespace DC_Lab1.DB
{
    public class CassandraDbContext : DbContext
    {
        public CassandraDbContext()
        { }

        public CassandraDbContext(DbContextOptions<CassandraDbContext> options)
            : base(options)
        { }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
           //optionsBuilder.UseCassandra("Host=localhost;Port=9042;Database=distcomp");
        }
    }
}