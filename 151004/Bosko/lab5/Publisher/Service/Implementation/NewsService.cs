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
    public class NewsService(IMapper mapper, InewsRepository repository, IDistributedCache cache)
        : AbstractCrudService<News, NewsRequestTO, NewsResponseTO>(mapper, repository), INewsService
    {
        public override async Task<bool> Remove(int id)
        {
            await cache.RemoveAsync(GetRedisId(id));

            return await base.Remove(id);
        }

        public override async Task<NewsResponseTO> GetByID(int id)
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

            return JsonConvert.DeserializeObject<NewsResponseTO>(cacheResponse)
                ?? throw new Exception("Unable to deserialize News");
        }

        public override async Task<NewsResponseTO> Add(NewsRequestTO newsTo)
        {
            if (!Validate(newsTo))
            {
                throw new InvalidDataException("News is not valid");
            }

            var res = await base.Add(newsTo);
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        public override async Task<NewsResponseTO> Update(NewsRequestTO newsTo)
        {
            if (!Validate(newsTo))
            {
                throw new InvalidDataException($"UPDATE invalid data: {newsTo}");
            }

            var res = await base.Update(newsTo);
            await cache.RemoveAsync(GetRedisId(res.Id));
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        private static string GetRedisId(int id) => $"News:{id}";

        public Task<NewsResponseTO> GetNewsByParam(IList<string> labelNames, IList<int> labelIds, string userLogin,
            string title, string content)
        {
            throw new NotImplementedException();
        }

        private static bool Validate(NewsRequestTO news)
        {
            var titleLen = news.Title.Length;
            var contentLen = news.Content.Length;

            if (titleLen < 2 || titleLen > 64)
            {
                return false;
            }
            if (contentLen < 4 || contentLen > 2048)
            {
                return false;
            }
            if (news.Modified < news.Created)
            {
                return false;
            }
            return true;
        }
    }
}
