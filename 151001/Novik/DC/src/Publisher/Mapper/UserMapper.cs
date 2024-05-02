using AutoMapper;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Models.Entity;

namespace Publisher.Mapper;

public class UserMapper : Profile
{
    public UserMapper()
    {
        CreateMap<User, UserResponseTo>()
            .ForMember(dest => dest.id, opt => opt.MapFrom(src => src.id))
            .ForMember(dest => dest.login, opt => opt.MapFrom(src => src.login))
            .ForMember(dest => dest.password, opt => opt.MapFrom(src => src.password))
            .ForMember(dest => dest.firstname, opt => opt.MapFrom(src => src.firstname))
            .ForMember(dest => dest.lastname, opt => opt.MapFrom(src => src.lastname));
        CreateMap<UserRequestTo, User>()
            .ForMember(dest => dest.id, opt => opt.MapFrom(src => src.id))
            .ForMember(dest => dest.login, opt => opt.MapFrom(src => src.login))
            .ForMember(dest => dest.password, opt => opt.MapFrom(src => src.password))
            .ForMember(dest => dest.firstname, opt => opt.MapFrom(src => src.firstname))
            .ForMember(dest => dest.lastname, opt => opt.MapFrom(src => src.lastname));
    }
    
}