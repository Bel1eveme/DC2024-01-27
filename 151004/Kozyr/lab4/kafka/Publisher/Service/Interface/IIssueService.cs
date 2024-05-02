using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Service.Interface.Common;

namespace Publisher.Service.Interface
{
    public interface IIssueService : ICrudService<Issue, IssueRequestTO, IssueResponseTO>
    {
        Task<IssueResponseTO> GetIssueByParam(IList<string> markerNames, IList<int> markerIds, string creatorLogin,
            string title, string content);
    }
}
