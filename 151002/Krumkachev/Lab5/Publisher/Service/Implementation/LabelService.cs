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
    public class LabelService(IMapper mapper, ILabelRepository repository, IDistributedCache cache)
        : AbstractCrudService<Label, LabelRequestTO, LabelResponseTO>(mapper, repository), ILabelService
    {
        public override async Task<bool> Remove(int id)
        {
            await cache.RemoveAsync(GetRedisId(id));

            return await base.Remove(id);
        }

        public override async Task<LabelResponseTO> GetByID(int id)
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

            return JsonConvert.DeserializeObject<LabelResponseTO>(cacheResponse)
                ?? throw new Exception("Unable to deserialize Label");
        }

        public override async Task<LabelResponseTO> Add(LabelRequestTO labelTo)
        {
            if (!Validate(labelTo))
            {
                throw new InvalidDataException("Label is not valid");
            }

            var res = await base.Add(labelTo);
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        public override async Task<LabelResponseTO> Update(LabelRequestTO labelTo)
        {
            if (!Validate(labelTo))
            {
                throw new InvalidDataException($"UPDATE invalid data: {labelTo}");
            }

            var res = await base.Update(labelTo);
            await cache.RemoveAsync(GetRedisId(res.Id));
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        private static string GetRedisId(int id) => $"Label:{id}";

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
