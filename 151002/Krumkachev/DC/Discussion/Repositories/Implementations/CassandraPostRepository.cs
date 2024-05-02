using Cassandra;
using Cassandra.Mapping;
using Discussion.Models;
using Discussion.Repositories.Interfaces;
using System.Reflection.Metadata.Ecma335;

namespace Discussion.Repositories.Implementations
{
	public class CassandraPostRepository : IRepository<Post>
	{
		private readonly Cluster _cluster = Cluster.Builder().AddContactPoint("localhost").Build();
		private readonly Cassandra.ISession _session;

		public CassandraPostRepository()
        {
			_session = _cluster.Connect("distcomp");
		}

		public async Task<IEnumerable<Post>> GetAllAsync()
		{
			return await new Mapper(_session).FetchAsync<Post>();
		}

		public async Task<Post?> GetByIdAsync(long id)
		{
			var cql = $"WHERE id = {id} ALLOW FILTERING";
			var post = await new Mapper(_session).FirstOrDefaultAsync<Post>(cql);
			return post.Id == id ? post : null;
		}

		public async Task<Post?> CreateAsync(Post entity)
		{
			var randomLong = new Random().NextInt64();
			entity.Id = randomLong;
			entity.Country = "Belarus";
			var result = await new Mapper(_session).InsertIfNotExistsAsync(entity);
			return result.Applied ? entity : null;
		}

		public async Task<Post?> UpdateAsync(Post entity)
		{
			var postInDb = await GetByIdAsync(entity.Id);
			if (postInDb == null)
			{
				return null;
			}
			entity.Country = postInDb.Country;
			await new Mapper(_session).UpdateAsync(entity);
			return entity;
		}

		public async Task<bool> DeleteAsync(Post entity)
		{
			var postInDb = await GetByIdAsync(entity.Id);
			if (postInDb == null)
			{
				return false;
			}
			await new Mapper(_session).DeleteAsync(entity);
			return true;
		}
		
	}
}
