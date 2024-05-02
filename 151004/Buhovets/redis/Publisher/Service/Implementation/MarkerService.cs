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
    public class TagService(IMapper mapper, ITagRepository repository, IDistributedCache cache)
        : AbstractCrudService<Tag, TagRequestTO, TagResponseTO>(mapper, repository), ITagService
    {
        public override async Task<bool> Remove(int id)
        {
            await cache.RemoveAsync(GetRedisId(id));

            return await base.Remove(id);
        }

        public override async Task<TagResponseTO> GetByID(int id)
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

            return JsonConvert.DeserializeObject<TagResponseTO>(cacheResponse)
                ?? throw new Exception("Unable to deserialize Tag");
        }

        public override async Task<TagResponseTO> Add(TagRequestTO tagTo)
        {
            if (!Validate(tagTo))
            {
                throw new InvalidDataException("Tag is not valid");
            }

            var res = await base.Add(tagTo);
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        public override async Task<TagResponseTO> Update(TagRequestTO tagTo)
        {
            if (!Validate(tagTo))
            {
                throw new InvalidDataException($"UPDATE invalid data: {tagTo}");
            }

            var res = await base.Update(tagTo);
            await cache.RemoveAsync(GetRedisId(res.Id));
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        private static string GetRedisId(int id) => $"Tag:{id}";

        public Task<IList<TagResponseTO>> GetByTweetID(int tweetId)
        {
            throw new NotImplementedException();
        }

        private static bool Validate(TagRequestTO tag)
        {
            var nameLen = tag.Name.Length;

            if (nameLen < 2 || nameLen > 32)
            {
                return false;
            }
            return true;
        }
    }
}
