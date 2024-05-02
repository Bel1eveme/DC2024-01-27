using AutoMapper;
using Discussion.Common;
using Discussion.NoteEntity.Dto;
using Discussion.NoteEntity.Interface;

namespace Discussion.NoteEntity
{
    public class NoteService(IMapper mapper, INoteRepository repository)
        : AbstractCrudService<Note, NoteRequestTO, NoteResponseTO>(mapper, repository), INoteService
    {
        public override async Task<NoteResponseTO> Add(NoteRequestTO note)
        {
            if (!Validate(note))
            {
                throw new InvalidDataException("NOTE is not valid");
            }

            return await base.Add(note);
        }

        public override async Task<NoteResponseTO> Update(NoteRequestTO note)
        {
            if (!Validate(note))
            {
                throw new InvalidDataException($"UPDATE invalid data: {note}");
            }

            return await base.Update(note);
        }

        public Task<IList<Note>> GetByNewsID(int newsId)
        {
            throw new NotImplementedException();
        }

        private static bool Validate(NoteRequestTO note)
        {
            var contentLen = note.Content.Length;

            if (contentLen < 2 || contentLen > 2048)
            {
                return false;
            }
            return true;
        }
    }
}
