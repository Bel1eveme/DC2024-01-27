using Discussion.Common.Interface;

namespace Discussion.NoteEntity.Interface
{
    public interface INoteService : ICrudService<Note, NoteRequestTO, NoteResponseTO>
    {
        Task<IList<Note>> GetByNewsID(int newsId);
    }
}
