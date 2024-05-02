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
    public class NewsService(IMapper mapper, INewsRepository repository)
        : AbstractCrudService<News, NewsRequestTO, NewsResponseTO>(mapper, repository), INewsService
    {
        public override async Task<NewsResponseTO> Add(NewsRequestTO news)
        {
            if (!Validate(news))
            {
                throw new InvalidDataException("NEWS is not valid");
            }

            return await base.Add(news);
        }

        public override async Task<NewsResponseTO> Update(NewsRequestTO news)
        {
            if (!Validate(news))
            {
                throw new InvalidDataException($"UPDATE invalid data: {news}");
            }

            return await base.Update(news);
        }

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
