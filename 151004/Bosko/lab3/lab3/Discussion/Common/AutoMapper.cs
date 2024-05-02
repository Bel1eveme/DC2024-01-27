using AutoMapper;
using Discussion.NoteEntity;

namespace Discussion.Common
{
    public class AutoMapper : Profile
    {
        public AutoMapper()
        {
            CreateMap<NoteRequestTO, Note>();
            CreateMap<Note, NoteResponseTO>();
        }
    }
}
