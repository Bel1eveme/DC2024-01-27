using Discussion.Common.Interface;
using Discussion.NoteEntity.Dto;

namespace Discussion.NoteEntity.Interface
{
    public interface INoteService : ICrudService<Note, NoteRequestTO, NoteResponseTO>
    {
        Task<IList<Note>> GetByNewsID(int newsId);
    }
}
