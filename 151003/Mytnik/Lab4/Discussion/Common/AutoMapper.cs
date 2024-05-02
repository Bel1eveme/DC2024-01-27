using AutoMapper;
using Discussion.NoteEntity;
using Discussion.NoteEntity.Dto;

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
