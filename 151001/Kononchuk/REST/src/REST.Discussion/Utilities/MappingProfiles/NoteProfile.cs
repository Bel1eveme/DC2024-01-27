using AutoMapper;
using REST.Discussion.Models.DTOs.Request;
using REST.Discussion.Models.DTOs.Response;
using REST.Discussion.Models.Entities;

namespace REST.Discussion.Utilities.MappingProfiles;

public class NoteProfile : Profile
{
    public NoteProfile()
    {
        CreateMap<Note, NoteResponseDto>();
        CreateMap<NoteRequestDto, Note>();
        // CreateMap<NoteRequestDto, Note>().ForMember(dest => dest.IssueId,
        //     opt => opt.MapFrom((dto, note) =>
        //         string.IsNullOrEmpty(dto.IssueId) ? note.IssueId = null : note.IssueId = Convert.ToInt64(dto.IssueId)));
        CreateMap<Note, NoteKey>();
    }
}