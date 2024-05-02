using AutoMapper;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.Models;

namespace Publisher.Infrastructure.Mapper;

public class MappingProfile : Profile
{
	public MappingProfile()
	{
		CreateMap<Creator, CreatorResponseDto>();
		CreateMap<CreatorRequestDto, Creator>();

		CreateMap<Issue, IssueResponseDto>();
		CreateMap<IssueRequestDto, Issue>();

		CreateMap<Label, LabelResponseDto>();
		CreateMap<LabelRequestDto, Label>();
	}
}