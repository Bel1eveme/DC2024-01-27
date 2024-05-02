using AutoMapper;
using Publisher.Infrastructure.Redis.Interfaces;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Models.Entity;
using Publisher.Repositories.interfaces;
using Publisher.Services.interfaces;

namespace Publisher.Services;

public class LabelService : ILabelService
{
    private readonly ILabelRepository _labelRepository;
    private readonly IMapper _labelMapper;
    private readonly ICacheService _cacheService;
    
    private const string Prefix = "label-";
    public LabelService(ILabelRepository labelRepository,IMapper labelMapper, ICacheService cacheService)
    {
        _labelMapper = labelMapper;
        _labelRepository = labelRepository;
        _cacheService = cacheService;
    }
    public async Task<IEnumerable<LabelResponseTo>> GetAllAsync()
    {
        var labels = (await _labelRepository.GetAllAsync()).ToList();
        var result = new List<LabelResponseTo>(labels.Count);

        foreach (var label in labels)
        {
            await _cacheService.SetAsync(Prefix + label.id, label);
            result.Add(_labelMapper.Map<LabelResponseTo>(label));
        }

        return result;
    }

    public async Task<LabelResponseTo?>? GetByIdAsync(long id)
    {
        var foundEditor = await _cacheService.GetAsync(Prefix + id,
            async () => await _labelRepository.GetByIdAsync(id));

        return _labelMapper.Map<LabelResponseTo>(foundEditor);
    }

    public async Task<LabelResponseTo> AddAsync(LabelRequestTo labelRequest)
    {
        var labelEntity = _labelMapper.Map<Label>(labelRequest);
        labelEntity = await _labelRepository.AddAsync(labelEntity);
        await _cacheService.SetAsync(Prefix + labelEntity.id, labelEntity);
        return _labelMapper.Map<LabelResponseTo>(labelEntity);
    }

    public async Task<LabelResponseTo> UpdateAsync(LabelRequestTo labelRequest)
    {
        if (labelRequest == null) throw new ArgumentNullException(nameof(labelRequest));
        var label = _labelMapper.Map<Label>(labelRequest);


        var updatedEditor = await _labelRepository.UpdateAsync(label);

        await _cacheService.SetAsync(Prefix + updatedEditor.id, updatedEditor);

        return _labelMapper.Map<LabelResponseTo>(updatedEditor);
    }

    public async Task DeleteAsync(long id)
    {
        await _cacheService.RemoveAsync(Prefix + id);
        if (!await _labelRepository.Exists(id))
        {
            throw new KeyNotFoundException("");
        }
        await _labelRepository.DeleteAsync(id);
    }
}