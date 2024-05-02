using AutoMapper;
using Microsoft.Extensions.Caching.Distributed;
using Newtonsoft.Json;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Repository.Interface;
using Publisher.Service.Implementation.Common;
using Publisher.Service.Interface;

namespace Publisher.Service.Implementation
{
    public class IssueService(IMapper mapper, IIssueRepository repository, IDistributedCache cache)
        : AbstractCrudService<Issue, IssueRequestTO, IssueResponseTO>(mapper, repository), IIssueService
    {
        public override async Task<bool> Remove(int id)
        {
            await cache.RemoveAsync(GetRedisId(id));

            return await base.Remove(id);
        }

        public override async Task<IssueResponseTO> GetByID(int id)
        {
            var cacheResponse = await cache.GetStringAsync(GetRedisId(id));

            if (cacheResponse is null)
            {
                var response = await base.GetByID(id);
                await cache.SetStringAsync(GetRedisId(id), JsonConvert.SerializeObject(response));

                return response;
            }

            _ = Task.Run(async () =>
            {
                var res = await base.GetByID(id);
                await cache.SetStringAsync(GetRedisId(id), JsonConvert.SerializeObject(res));
            });

            return JsonConvert.DeserializeObject<IssueResponseTO>(cacheResponse)
                ?? throw new Exception("Unable to deserialize Issue");
        }

        public override async Task<IssueResponseTO> Add(IssueRequestTO issueTo)
        {
            if (!Validate(issueTo))
            {
                throw new InvalidDataException("Issue is not valid");
            }

            var res = await base.Add(issueTo);
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        public override async Task<IssueResponseTO> Update(IssueRequestTO issueTo)
        {
            if (!Validate(issueTo))
            {
                throw new InvalidDataException($"UPDATE invalid data: {issueTo}");
            }

            var res = await base.Update(issueTo);
            await cache.RemoveAsync(GetRedisId(res.Id));
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        private static string GetRedisId(int id) => $"Issue:{id}";

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
