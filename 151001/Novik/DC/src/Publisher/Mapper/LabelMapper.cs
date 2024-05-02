using AutoMapper;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Models.Entity;

namespace Publisher.Mapper;

public class LabelMapper:Profile
{
    public LabelMapper()
    {
        CreateMap<Label, LabelResponseTo>()
            .ForMember(dest => dest.id, opt => opt.MapFrom(src => src.id))
            .ForMember(dest => dest.name, opt => opt.MapFrom(src => src.name));
        CreateMap<LabelRequestTo, Label>()
            .ForMember(dest => dest.id, opt => opt.MapFrom(src => src.id))
            .ForMember(dest => dest.name, opt => opt.MapFrom(src => src.name));
    }
}