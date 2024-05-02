using AutoMapper;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;

namespace Publisher.Entity.Common
{
    public class AutoMapper : Profile
    {
        public AutoMapper()
        {
            CreateMap<CreatorRequestTO, Creator>();
            CreateMap<Creator, CreatorResponseTO>();

            CreateMap<LabelRequestTO, Label>();
            CreateMap<Label, LabelResponseTO>();

            CreateMap<IssueRequestTO, Issue>();
            CreateMap<Issue, IssueResponseTO>();
        }
    }
}
