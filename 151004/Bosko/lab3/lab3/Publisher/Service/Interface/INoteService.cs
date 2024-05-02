using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Service.Interface.Common;

namespace Publisher.Service.Interface
{
    public interface INoteService : ICrudService<Note, NoteRequestTO, NoteResponseTO>
    {
        Task<IList<Note>> GetByNewsID(int newsId);
    }
}
