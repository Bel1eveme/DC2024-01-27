using Publisher.Entity.Db;
using Publisher.Repository.Implementation.Common;
using Publisher.Repository.Interface;
using Publisher.Storage.Common;

namespace Publisher.Repository.Implementation
{
    public class NoteRepository(DbStorage context) : AbstractCrudRepository<Note>(context), INoteRepository
    {
        private readonly DbStorage _context = context;

        public override Note Add(Note note)
        {
            var news = new News { Id = note.NewsId };
            _context.News.Attach(news);
            note.News = news;

            return base.Add(note);
        }

        public override async Task<Note> AddAsync(Note note)
        {
            var news = new News { Id = note.NewsId };
            _context.News.Attach(news);
            note.News = news;

            return await base.AddAsync(note);
        }
    }
}
