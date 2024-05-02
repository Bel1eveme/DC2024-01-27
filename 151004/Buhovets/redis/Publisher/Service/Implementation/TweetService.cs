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
    public class TweetService(IMapper mapper, ITweetRepository repository, IDistributedCache cache)
        : AbstractCrudService<Tweet, TweetRequestTO, TweetResponseTO>(mapper, repository), ITweetService
    {
        public override async Task<bool> Remove(int id)
        {
            await cache.RemoveAsync(GetRedisId(id));

            return await base.Remove(id);
        }

        public override async Task<TweetResponseTO> GetByID(int id)
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

            return JsonConvert.DeserializeObject<TweetResponseTO>(cacheResponse)
                ?? throw new Exception("Unable to deserialize Tweet");
        }

        public override async Task<TweetResponseTO> Add(TweetRequestTO tweetTo)
        {
            if (!Validate(tweetTo))
            {
                throw new InvalidDataException("Tweet is not valid");
            }

            var res = await base.Add(tweetTo);
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        public override async Task<TweetResponseTO> Update(TweetRequestTO tweetTo)
        {
            if (!Validate(tweetTo))
            {
                throw new InvalidDataException($"UPDATE invalid data: {tweetTo}");
            }

            var res = await base.Update(tweetTo);
            await cache.RemoveAsync(GetRedisId(res.Id));
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        private static string GetRedisId(int id) => $"Tweet:{id}";

        public Task<TweetResponseTO> GetTweetByParam(IList<string> tagNames, IList<int> tagIds, string authorLogin,
            string title, string content)
        {
            throw new NotImplementedException();
        }

        private static bool Validate(TweetRequestTO tweet)
        {
            var titleLen = tweet.Title.Length;
            var contentLen = tweet.Content.Length;

            if (titleLen < 2 || titleLen > 64)
            {
                return false;
            }
            if (contentLen < 4 || contentLen > 2048)
            {
                return false;
            }
            if (tweet.Modified < tweet.Created)
            {
                return false;
            }
            return true;
        }
    }
}
