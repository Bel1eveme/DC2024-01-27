using Cassandra.Mapping;
using REST.Discussion.Data;
using REST.Discussion.Models.Entities;
using REST.Discussion.Repositories.Interfaces;
using REST.Discussion.Utilities.Exceptions;

namespace REST.Discussion.Repositories.Implementations;

public class NoteRepository(CassandraContext context) : INoteRepository<NoteKey>
{
    private readonly Mapper _mapper = new(context.Session);

    public async Task<Note> AddAsync(Note entity)
    {
        ArgumentNullException.ThrowIfNull(entity);

        await _mapper.InsertAsync(entity);

        return entity;
    }

    public async Task<Note> GetByIdAsync(NoteKey id)
    {
        Note? result = (await GetAllByKeyAsync(id)).FirstOrDefault();

        if (result is null)
        {
            throw new ResourceNotFoundException(code: 40401);
        }

        return result;
    }

    public async Task<IEnumerable<Note>> GetAllAsync()
    {
        return await _mapper.FetchAsync<Note>();
    }

    public async Task<Note> UpdateAsync(NoteKey id, Note entity)
    {
        ArgumentNullException.ThrowIfNull(entity);

        Note? result = (await GetAllByKeyAsync(id)).FirstOrDefault();

        if (result is null)
        {
            throw new ResourceNotFoundException(code: 40402);
        }

        result.Content = entity.Content;
        await _mapper.UpdateAsync(result);

        return result;
    }

    public async Task DeleteAsync(NoteKey id)
    {
        Note? result = (await GetAllByKeyAsync(id)).FirstOrDefault();

        if (result is null)
        {
            throw new ResourceNotFoundException(code: 40403);
        }

        await _mapper.DeleteAsync(result);
    }

    private async Task<IEnumerable<Note>> GetAllByKeyAsync(NoteKey key)
    {
        List<string> query = [];
        List<object> args = [];
    
        if (key.Country is not null)
        {
            query.Add("country = ?");
            args.Add(key.Country);
        }
        if (key.IssueId is not null)
        {
            query.Add("issueId = ?");
            args.Add(key.IssueId);
        }
        if (key.Id is not null)
        {
            query.Add("id = ?");
            args.Add(key.Id);
        }

        return await _mapper.FetchAsync<Note>("WHERE " + string.Join(" AND ", query) + " ALLOW FILTERING",
            args.ToArray());
    }
}