using Cassandra.Mapping;
using Discussion.Common;
using Discussion.CommentEntity.Interface;
using Discussion.Storage.Cassandra;

namespace Discussion.CommentEntity
{
    public class CommentRepository(CassandraStorage storage, Random random) : AbstractCrudRepository<Comment>(storage, random), ICommentRepository
    {
        public override Comment Add(Comment entity)
        {
            entity.Country = "Belarus";

            return base.Add(entity);
        }

        public override async Task<Comment> AddAsync(Comment entity)
        {
            entity.Country = "Belarus";

            return await base.AddAsync(entity);
        }

        public override async Task<Comment> UpdateAsync(Comment entity)
        {
            entity.Country = "Belarus";

            return await base.UpdateAsync(entity);
        }
    }
}
