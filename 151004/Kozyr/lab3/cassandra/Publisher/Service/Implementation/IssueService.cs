using AutoMapper;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Repository.Interface;
using Publisher.Service.Implementation.Common;
using Publisher.Service.Interface;
using Publisher.Storage.Common;

namespace Publisher.Service.Implementation
{
    public class IssueService(IMapper mapper, IIssueRepository repository)
        : AbstractCrudService<Issue, IssueRequestTO, IssueResponseTO>(mapper, repository), IIssueService
    {
        public override async Task<IssueResponseTO> Add(IssueRequestTO issue)
        {
            if (!Validate(issue))
            {
                throw new InvalidDataException("TWEET is not valid");
            }

            return await base.Add(issue);
        }

        public override async Task<IssueResponseTO> Update(IssueRequestTO issue)
        {
            if (!Validate(issue))
            {
                throw new InvalidDataException($"UPDATE invalid data: {issue}");
            }

            return await base.Update(issue);
        }

        public Task<IssueResponseTO> GetIssueByParam(IList<string> markerNames, IList<int> markerIds, string creatorLogin,
            string title, string content)
        {
            throw new NotImplementedException();
        }

        private static bool Validate(IssueRequestTO issue)
        {
            var titleLen = issue.Title.Length;
            var contentLen = issue.Content.Length;

            if (titleLen < 2 || titleLen > 64)
            {
                return false;
            }
            if (contentLen < 4 || contentLen > 2048)
            {
                return false;
            }
            if (issue.Modified < issue.Created)
            {
                return false;
            }
            return true;
        }
    }
}
