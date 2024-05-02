using DC_REST.Data;
using DC_REST.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Caching.Distributed;
using Newtonsoft.Json;

namespace DC_REST.Repositories
{
	public class UserDbRepository: IRepository<User>
	{
		private readonly DatabaseContext _dbContext;
		private readonly IDistributedCache _distributedCache;

		public UserDbRepository(DatabaseContext dbContext, IDistributedCache distributedCache)
		{
			_dbContext = dbContext;
			_distributedCache = distributedCache;
		}

		public User GetById(int id)
		{
			string key = $"user-{id}";
			string? cachedUser = _distributedCache.GetString(key);

			User? user;
			if (string.IsNullOrEmpty(cachedUser))
			{
				user = _dbContext.Users.Find(id);
				if (user == null)
				{
					return user;
				}

				_distributedCache.SetString(key, JsonConvert.SerializeObject(user));
				return user;
			}
			user = JsonConvert.DeserializeObject<User>(cachedUser);
			return user;
		}

		public List<User> GetAll()
		{
			return _dbContext.Users.ToList();
		}

		public User Add(User user)
		{
			try
			{
				_dbContext.Users.Add(user);
				_dbContext.SaveChanges();

				string key = $"user-{user.Id}";
				_distributedCache.SetString(key, JsonConvert.SerializeObject(user));

				return user;
			}
			catch (Exception)
			{
				throw new DbUpdateException("Violation of uniqueness constraint");
			}
		}

		public User Update(int id, User user)
		{
			var existingUser = _dbContext.Users.Find(id);

			if (existingUser == null)
			{
				throw new ArgumentException("There is no such issue");
			}

			try
			{
				_dbContext.SaveChanges();

				string key = $"user-{user.Id}";
				_distributedCache.SetString(key, JsonConvert.SerializeObject(user));

			}
			catch (DbUpdateException)
			{
				throw new DbUpdateException("Violation of uniqueness constraint");
			}

			return user;
		}

		public bool Delete(int id)
		{
			var user = _dbContext.Users.Find(id);
			if (user == null)
				return false;

			string key = $"user-{user.Id}";
			string? cachedUser = _distributedCache.GetString(key);
			if (!string.IsNullOrEmpty(cachedUser)) _distributedCache.Remove(key);

			_dbContext.Users.Remove(user);
			_dbContext.SaveChanges();
			return true;
		}

		public int GetCurrentId()
		{
			throw new NotImplementedException();
		}
	}
}
