using AutoMapper;
using FluentValidation;
using REST.Publisher.Infrastructure.Redis.Interfaces;
using REST.Publisher.Models.DTOs.Request;
using REST.Publisher.Models.DTOs.Response;
using REST.Publisher.Models.Entities;
using REST.Publisher.Repositories.Interfaces;
using REST.Publisher.Services.Interfaces;
using Exceptions_ValidationException = REST.Publisher.Utilities.Exceptions.ValidationException;

namespace REST.Publisher.Services.Implementations;

public class EditorService(
    IMapper mapper,
    IEditorRepository<long> editorRepository,
    AbstractValidator<Editor> validator,
    ICacheService cacheService) : IEditorService
{
    private const string Prefix = "editor-";

    public async Task<EditorResponseDto> CreateAsync(EditorRequestDto dto)
    {
        if (dto == null) throw new ArgumentNullException(nameof(dto));

        var editor = mapper.Map<Editor>(dto);

        var validationResult = await validator.ValidateAsync(editor);

        if (!validationResult.IsValid)
        {
            throw new Exceptions_ValidationException("Editor data has not been validated", 40001);
        }

        var createdEditor = await editorRepository.AddAsync(editor);

        await cacheService.SetAsync(Prefix + createdEditor.Id, createdEditor);

        return mapper.Map<EditorResponseDto>(createdEditor);
    }

    public async Task<EditorResponseDto> GetByIdAsync(long id)
    {
        var foundEditor = await cacheService.GetAsync(Prefix + id,
            async () => await editorRepository.GetByIdAsync(id));

        return mapper.Map<EditorResponseDto>(foundEditor);
    }

    public async Task<IEnumerable<EditorResponseDto>> GetAllAsync()
    {
        var editors = (await editorRepository.GetAllAsync()).ToList();
        var result = new List<EditorResponseDto>(editors.Count);

        foreach (var editor in editors)
        {
            await cacheService.SetAsync(Prefix + editor.Id, editor);
            result.Add(mapper.Map<EditorResponseDto>(editor));
        }

        return result;
    }

    public async Task<EditorResponseDto> UpdateAsync(long id, EditorRequestDto dto)
    {
        if (dto == null) throw new ArgumentNullException(nameof(dto));
        var editor = mapper.Map<Editor>(dto);

        var validationResult = await validator.ValidateAsync(editor);

        if (!validationResult.IsValid)
        {
            throw new Exceptions_ValidationException("Editor data has not been validated", 40002);
        }

        var updatedEditor = await editorRepository.UpdateAsync(id, editor);

        await cacheService.SetAsync(Prefix + updatedEditor.Id, updatedEditor);

        return mapper.Map<EditorResponseDto>(updatedEditor);
    }

    public async Task DeleteAsync(long id)
    {
        await editorRepository.DeleteAsync(id);
        await cacheService.RemoveAsync(Prefix + id);
    }
}