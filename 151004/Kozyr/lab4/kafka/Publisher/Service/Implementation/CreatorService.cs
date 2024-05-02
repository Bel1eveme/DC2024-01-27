using AutoMapper;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Repository.Interface;
using Publisher.Service.Implementation.Common;
using Publisher.Service.Interface;

namespace Publisher.Service.Implementation
{
    public class CreatorService(IMapper mapper, ICreatorRepository repository) :
        AbstractCrudService<Creator, CreatorRequestTO, CreatorResponseTO>(mapper, repository), ICreatorService
    {
        private readonly IMapper _mapper = mapper;

        public override async Task<CreatorResponseTO> Add(CreatorRequestTO creatorTo)
        {
            if (!Validate(creatorTo))
            {
                throw new InvalidDataException("Creator is not valid");
            }

            return await base.Add(creatorTo);
        }

        public override async Task<CreatorResponseTO> Update(CreatorRequestTO creatorTo)
        {
            if (!Validate(creatorTo))
            {
                throw new InvalidDataException($"UPDATE invalid data: {creatorTo}");
            }

            return await base.Update(creatorTo);
        }

        public async Task<CreatorResponseTO> GetByIssueID(int issueId)
        {
            var response = await repository.GetByIssueIdAsync(issueId);

            return _mapper.Map<CreatorResponseTO>(response.Creator);
        }

        private static bool Validate(CreatorRequestTO creator)
        {
            var fnameLen = creator.FirstName.Length;
            var lnameLen = creator.LastName.Length;
            var passLen = creator.Password.Length;
            var loginLen = creator.Login.Length;

            if (fnameLen < 2 || fnameLen > 64)
            {
                return false;
            }
            if (lnameLen < 2 || fnameLen > 64)
            {
                return false;
            }
            if (passLen < 8 || passLen > 128)
            {
                return false;
            }
            if (loginLen < 2 || loginLen > 64)
            {
                return false;
            }
            return true;
        }
    }
}
