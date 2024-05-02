using AutoMapper;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Models.Entity;

namespace Publisher.Mapper;

public class NewsMapper : Profile
{
    public NewsMapper()
    {
        CreateMap<News, NewsResponseTo>()
            .ForMember(dest => dest.id, opt => opt.MapFrom(src => src.id))
            .ForMember(dest => dest.title, opt => opt.MapFrom(src => src.title))
            .ForMember(dest => dest.userId, opt => opt.MapFrom(src => src.userId))
            .ForMember(dest => dest.content, opt => opt.MapFrom(src => src.content))
            .ForMember(dest => dest.created, opt => opt.MapFrom(src => src.created))
            .ForMember(dest => dest.modified, opt => opt.MapFrom(src => src.modified));
        CreateMap<NewsRequestTo, News>()
            .ForMember(dest => dest.id, opt => opt.MapFrom(src => src.id))
            .ForMember(dest => dest.title, opt => opt.MapFrom(src => src.title))
            .ForMember(dest => dest.userId, opt => opt.MapFrom(src => src.userId))
            .ForMember(dest => dest.content, opt => opt.MapFrom(src => src.content))
            .ForMember(dest => dest.created, opt => opt.MapFrom(src => src.created))
            .ForMember(dest => dest.modified, opt => opt.MapFrom(src => src.modified));
    }
    
}