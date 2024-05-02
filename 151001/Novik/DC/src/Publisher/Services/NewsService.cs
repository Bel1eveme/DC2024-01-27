using AutoMapper;
using Publisher.Infrastructure.Redis.Interfaces;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Models.Entity;
using Publisher.Repositories.interfaces;
using Publisher.Services.interfaces;

namespace Publisher.Services;

public class NewsService : INewsService
{
    private readonly INewsRepository _newsRepository;
    private readonly IMapper _newsMapper;
    private readonly ICacheService _cacheService;
    
    private const string Prefix = "news-";
    public NewsService(INewsRepository newsRepository,IMapper newsMapper, ICacheService cacheService)
    {
        _newsMapper = newsMapper;
        _newsRepository = newsRepository;
        _cacheService = cacheService;
    }
    public async Task<IEnumerable<NewsResponseTo>> GetAllAsync()
    {
        var newss = (await _newsRepository.GetAllAsync()).ToList();
        var result = new List<NewsResponseTo>(newss.Count);

        foreach (var news in newss)
        {
            await _cacheService.SetAsync(Prefix + news.id, news);
            result.Add(_newsMapper.Map<NewsResponseTo>(news));
        }

        return result;
    }

    public async Task<NewsResponseTo?>? GetByIdAsync(long id)
    {
        var foundEditor = await _cacheService.GetAsync(Prefix + id,
            async () => await _newsRepository.GetByIdAsync(id));

        return _newsMapper.Map<NewsResponseTo>(foundEditor);
    }

    public async Task<NewsResponseTo> AddAsync(NewsRequestTo newsRequest)
    {
        var newsEntity = _newsMapper.Map<News>(newsRequest);
        newsEntity = await _newsRepository.AddAsync(newsEntity);
        await _cacheService.SetAsync(Prefix + newsEntity.id, newsEntity);
        return _newsMapper.Map<NewsResponseTo>(newsEntity);
    }

    public async Task<NewsResponseTo> UpdateAsync(NewsRequestTo newsRequest)
    {
        if (newsRequest == null) throw new ArgumentNullException(nameof(newsRequest));
        var news = _newsMapper.Map<News>(newsRequest);


        var updatedEditor = await _newsRepository.UpdateAsync(news);

        await _cacheService.SetAsync(Prefix + updatedEditor.id, updatedEditor);

        return _newsMapper.Map<NewsResponseTo>(updatedEditor);
    }

    public async Task DeleteAsync(long id)
    {
        await _cacheService.RemoveAsync(Prefix + id);
        await _newsRepository.DeleteAsync(id);
    }
    
}