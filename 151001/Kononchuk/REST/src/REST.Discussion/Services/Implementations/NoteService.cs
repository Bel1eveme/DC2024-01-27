using AutoMapper;
using FluentValidation;
using REST.Discussion.Models.DTOs.Request;
using REST.Discussion.Models.DTOs.Response;
using REST.Discussion.Models.Entities;
using REST.Discussion.Repositories.Interfaces;
using REST.Discussion.Services.Interfaces;
using Exceptions_ValidationException = REST.Discussion.Utilities.Exceptions.ValidationException;

namespace REST.Discussion.Services.Implementations;

public class NoteService(
    IMapper mapper,
    INoteRepository<NoteKey> noteRepository,
    AbstractValidator<Note> validator) : INoteService
{
    public async Task<NoteResponseDto> CreateAsync(NoteRequestDto dto)
    {
        if (dto == null) throw new ArgumentNullException(nameof(dto));
        var note = mapper.Map<Note>(dto);
        note.Id = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();
        
        var validationResult = await validator.ValidateAsync(note);

        if (!validationResult.IsValid)
        {
            throw new Exceptions_ValidationException("Note data has not been validated", 40001);
        }

        var createdNote = await noteRepository.AddAsync(note);

        return mapper.Map<NoteResponseDto>(createdNote);
    }


    public async Task<NoteResponseDto> GetByIdAsync(NoteKey id)
    {
        var foundNote = await noteRepository.GetByIdAsync(id);

        return mapper.Map<NoteResponseDto>(foundNote);
    }

    public async Task<IEnumerable<NoteResponseDto>> GetAllAsync()
    {
        return (await noteRepository.GetAllAsync()).Select(mapper.Map<NoteResponseDto>).ToList();
    }

    public async Task<NoteResponseDto> UpdateAsync(NoteRequestDto dto)
    {
        if (dto == null) throw new ArgumentNullException(nameof(dto));
        var note = mapper.Map<Note>(dto);

        var validationResult = await validator.ValidateAsync(note);

        if (!validationResult.IsValid)
        {
            throw new Exceptions_ValidationException("Note data has not been validated", 40002);
        }

        NoteKey key = mapper.Map<NoteKey>(note);
        var updatedNote = await noteRepository.UpdateAsync(key, note);

        return mapper.Map<NoteResponseDto>(updatedNote);
    }

    public async Task DeleteAsync(NoteKey id)
    {
        await noteRepository.DeleteAsync(id);
    }
}