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
    public class MarkerService(IMapper mapper, IMarkerRepository repository, IDistributedCache cache)
        : AbstractCrudService<Marker, MarkerRequestTO, MarkerResponseTO>(mapper, repository), IMarkerService
    {
        public override async Task<bool> Remove(int id)
        {
            await cache.RemoveAsync(GetRedisId(id));

            return await base.Remove(id);
        }

        public override async Task<MarkerResponseTO> GetByID(int id)
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

            return JsonConvert.DeserializeObject<MarkerResponseTO>(cacheResponse)
                ?? throw new Exception("Unable to deserialize Marker");
        }

        public override async Task<MarkerResponseTO> Add(MarkerRequestTO markerTo)
        {
            if (!Validate(markerTo))
            {
                throw new InvalidDataException("Marker is not valid");
            }

            var res = await base.Add(markerTo);
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        public override async Task<MarkerResponseTO> Update(MarkerRequestTO markerTo)
        {
            if (!Validate(markerTo))
            {
                throw new InvalidDataException($"UPDATE invalid data: {markerTo}");
            }

            var res = await base.Update(markerTo);
            await cache.RemoveAsync(GetRedisId(res.Id));
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        private static string GetRedisId(int id) => $"Marker:{id}";

        public Task<IList<MarkerResponseTO>> GetByIssueID(int issueId)
        {
            throw new NotImplementedException();
        }

        private static bool Validate(MarkerRequestTO marker)
        {
            var nameLen = marker.Name.Length;

            if (nameLen < 2 || nameLen > 32)
            {
                return false;
            }
            return true;
        }
    }
}
