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
    public class CreatorService(IMapper mapper, ICreatorRepository repository, IDistributedCache cache) :
        AbstractCrudService<Creator, CreatorRequestTO, CreatorResponseTO>(mapper, repository), ICreatorService
    {
        private readonly IMapper _mapper = mapper;

        public override async Task<bool> Remove(int id)
        {
            await cache.RemoveAsync(GetRedisId(id));

            return await base.Remove(id);
        }

        public override async Task<CreatorResponseTO> GetByID(int id)
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

            return JsonConvert.DeserializeObject<CreatorResponseTO>(cacheResponse)
                ?? throw new Exception("Unable to deserialize Creator");
        }

        public override async Task<CreatorResponseTO> Add(CreatorRequestTO creatorTo)
        {
            if (!Validate(creatorTo))
            {
                throw new InvalidDataException("Creator is not valid");
            }

            var res = await base.Add(creatorTo);
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        public override async Task<CreatorResponseTO> Update(CreatorRequestTO creatorTo)
        {
            if (!Validate(creatorTo))
            {
                throw new InvalidDataException($"UPDATE invalid data: {creatorTo}");
            }

            var res = await base.Update(creatorTo);
            await cache.RemoveAsync(GetRedisId(res.Id));
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        private static string GetRedisId(int id) => $"Creator:{id}";

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
