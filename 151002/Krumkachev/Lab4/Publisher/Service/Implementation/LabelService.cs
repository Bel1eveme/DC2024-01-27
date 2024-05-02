using AutoMapper;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Repository.Interface;
using Publisher.Service.Implementation.Common;
using Publisher.Service.Interface;

namespace Publisher.Service.Implementation
{
    public class LabelService(IMapper mapper, ILabelRepository repository)
        : AbstractCrudService<Label, LabelRequestTO, LabelResponseTO>(mapper, repository), ILabelService
    {
        public override async Task<LabelResponseTO> Add(LabelRequestTO label)
        {
            if (!Validate(label))
            {
                throw new InvalidDataException("MARKER is not valid");
            }

            return await base.Add(label);
        }

        public override async Task<LabelResponseTO> Update(LabelRequestTO label)
        {
            if (!Validate(label))
            {
                throw new InvalidDataException($"UPDATE invalid data: {label}");
            }

            return await base.Update(label);
        }

        public Task<IList<LabelResponseTO>> GetByIssueID(int issueId)
        {
            throw new NotImplementedException();
        }

        private static bool Validate(LabelRequestTO label)
        {
            var nameLen = label.Name.Length;

            if (nameLen < 2 || nameLen > 32)
            {
                return false;
            }
            return true;
        }
    }
}
