using Cassandra.Mapping;
using Discussion.Common;
using Discussion.NoteEntity.Interface;
using Discussion.Storage.Cassandra;

namespace Discussion.NoteEntity
{
    public class NoteRepository(CassandraStorage storage, Random random) : AbstractCrudRepository<Note>(storage, random), INoteRepository
    {
        public override Note Add(Note entity)
        {
            entity.Country = "Belarus";

            return base.Add(entity);
        }

        public override async Task<Note> AddAsync(Note entity)
        {
            entity.Country = "Belarus";

            return await base.AddAsync(entity);
        }

        public override async Task<Note> UpdateAsync(Note entity)
        {
            entity.Country = "Belarus";

            return await base.UpdateAsync(entity);
        }
    }
}
