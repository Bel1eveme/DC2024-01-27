using AutoMapper;
using FluentAssertions;
using FluentValidation;
using FluentValidation.Results;
using JetBrains.Annotations;
using Moq;
using REST.Discussion.Models.DTOs.Request;
using REST.Discussion.Models.DTOs.Response;
using REST.Discussion.Models.Entities;
using REST.Discussion.Repositories.Interfaces;
using REST.Discussion.Services.Implementations;
using REST.Discussion.Services.Interfaces;
using REST.Discussion.Utilities.Exceptions;
using ValidationException = REST.Discussion.Utilities.Exceptions.ValidationException;


namespace REST.Discussion.UnitTests.Services.Implementations;

[TestSubject(typeof(NoteService))]
public class NoteServiceTest
{
    private readonly Mock<IMapper> _mapperMock = new();
    private readonly Mock<INoteRepository<NoteKey>> _repositoryMock = new();
    private readonly Mock<AbstractValidator<Note>> _validatorMock = new();
    private readonly INoteService _noteService;

    public NoteServiceTest()
    {
        _noteService = new NoteService(_mapperMock.Object, _repositoryMock.Object, _validatorMock.Object);
    }


    [Fact]
    public async Task CreateAsync_NullArgument_ThrowsArgumentNullException()
    {
        Func<Task> actual = async () => await _noteService.CreateAsync(null!);

        await actual.Should().ThrowExactlyAsync<ArgumentNullException>();
    }

    [Fact]
    public async Task CreateAsync_InvalidArgument_ThrowsValidationException()
    {
        NoteRequestDto noteRequestDto = new();
        Note note = new();
        ValidationResult validationResult =
            new ValidationResult(new List<ValidationFailure>([new ValidationFailure()]));
        _mapperMock.Setup(mapper => mapper.Map<Note>(It.IsAny<NoteRequestDto>())).Returns(note);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Note>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);

        Func<Task> actual = async () => await _noteService.CreateAsync(noteRequestDto);

        await actual.Should().ThrowExactlyAsync<ValidationException>();
        _mapperMock.Verify(mapper => mapper.Map<Note>(It.IsAny<NoteRequestDto>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Note>>(), It.IsAny<CancellationToken>()),
            Times.Once);
    }

    [Fact]
    public async Task CreateAsync_ValidArgument_ReturnsAddedNoteResponseDto()
    {
        NoteRequestDto noteRequestDto = new();
        NoteResponseDto noteResponseDto = new();
        Note note = new();
        ValidationResult validationResult = new();
        _mapperMock.Setup(mapper => mapper.Map<Note>(It.IsAny<NoteRequestDto>())).Returns(note);
        _mapperMock.Setup(mapper => mapper.Map<NoteResponseDto>(It.IsAny<Note>())).Returns(noteResponseDto);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Note>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);
        _repositoryMock.Setup(repository => repository.AddAsync(It.IsAny<Note>())).ReturnsAsync(note);

        var result = await _noteService.CreateAsync(noteRequestDto);

        result.Should().Be(noteResponseDto);
        _mapperMock.Verify(mapper => mapper.Map<Note>(It.IsAny<NoteRequestDto>()), Times.Once);
        _mapperMock.Verify(mapper => mapper.Map<NoteResponseDto>(It.IsAny<Note>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Note>>(), It.IsAny<CancellationToken>()),
            Times.Once);
        _repositoryMock.Verify(repository => repository.AddAsync(It.IsAny<Note>()), Times.Once);
    }

    [Fact]
    public async Task GetByIdAsync_NoteNotExist_ThrowsResourceNotFoundException()
    {
        _repositoryMock.Setup(repository => repository.GetByIdAsync(It.IsAny<NoteKey>()))
            .ThrowsAsync(new ResourceNotFoundException());

        Func<Task> actual = async () => await _noteService.GetByIdAsync(new NoteKey{Id = -1});

        await actual.Should().ThrowExactlyAsync<ResourceNotFoundException>();
        _repositoryMock.Verify(repository => repository.GetByIdAsync(It.IsAny<NoteKey>()), Times.Once);
    }

    [Fact]
    public async Task GetByIdAsync_NoteExist_ReturnsExistingResult()
    {
        Note note = new Note();
        NoteResponseDto noteResponseDto = new();
        _repositoryMock.Setup(repository => repository.GetByIdAsync(It.IsAny<NoteKey>()))
            .ReturnsAsync(note);
        _mapperMock.Setup(mapper => mapper.Map<NoteResponseDto>(It.IsAny<Note>())).Returns(noteResponseDto);

        var result = await _noteService.GetByIdAsync(new NoteKey{Id = 1});

        result.Should().Be(noteResponseDto);
        _repositoryMock.Verify(repository => repository.GetByIdAsync(It.IsAny<NoteKey>()), Times.Once);
    }

    [Fact]
    public async Task GetAllAsync_EmptyRepository_ReturnsEmptyList()
    {
        NoteResponseDto noteResponseDto = new();
        _repositoryMock.Setup(repository => repository.GetAllAsync())
            .ReturnsAsync([]);
        _mapperMock.Setup(mapper => mapper.Map<NoteResponseDto>(It.IsAny<Note>())).Returns(noteResponseDto);

        var result = await _noteService.GetAllAsync();

        result.Should().BeEmpty();
        _repositoryMock.Verify(repository => repository.GetAllAsync(), Times.Once);
        _mapperMock.Verify(mapper => mapper.Map<NoteResponseDto>(It.IsAny<Note>()), Times.Never);
    }

    [Fact]
    public async Task UpdateAsync_NullArgument_ThrowsArgumentNullException()
    {
        Func<Task> actual = async () => await _noteService.UpdateAsync(null!);

        await actual.Should().ThrowExactlyAsync<ArgumentNullException>();
    }

    [Fact]
    public async Task UpdateAsync_InvalidArgument_ThrowsValidationException()
    {
        NoteRequestDto noteRequestDto = new();
        Note note = new();
        ValidationResult validationResult =
            new ValidationResult(new List<ValidationFailure>([new ValidationFailure()]));
        _mapperMock.Setup(mapper => mapper.Map<Note>(It.IsAny<NoteRequestDto>())).Returns(note);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Note>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);

        Func<Task> actual = async () => await _noteService.UpdateAsync(noteRequestDto);

        await actual.Should().ThrowExactlyAsync<ValidationException>();
        _mapperMock.Verify(mapper => mapper.Map<Note>(It.IsAny<NoteRequestDto>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Note>>(), It.IsAny<CancellationToken>()),
            Times.Once);
    }

    [Fact]
    public async Task UpdateAsync_ValidArgument_ReturnsAddedNoteResponseDto()
    {
        NoteRequestDto noteRequestDto = new();
        NoteResponseDto noteResponseDto = new();
        Note note = new();
        ValidationResult validationResult = new();
        _mapperMock.Setup(mapper => mapper.Map<Note>(It.IsAny<NoteRequestDto>())).Returns(note);
        _mapperMock.Setup(mapper => mapper.Map<NoteResponseDto>(It.IsAny<Note>())).Returns(noteResponseDto);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Note>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);
        _repositoryMock.Setup(repository => repository.UpdateAsync(It.IsAny<NoteKey>(), It.IsAny<Note>()))
            .ReturnsAsync(note);

        var result = await _noteService.UpdateAsync(noteRequestDto);

        result.Should().Be(noteResponseDto);
        _mapperMock.Verify(mapper => mapper.Map<Note>(It.IsAny<NoteRequestDto>()), Times.Once);
        _mapperMock.Verify(mapper => mapper.Map<NoteResponseDto>(It.IsAny<Note>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Note>>(), It.IsAny<CancellationToken>()),
            Times.Once);
        _repositoryMock.Verify(repository => repository.UpdateAsync(It.IsAny<NoteKey>(), It.IsAny<Note>()), Times.Once);
    }

    [Fact]
    public async Task Delete_NoteNotExist_ThrowsResourceNotFoundException()
    {
        _repositoryMock.Setup(repository => repository.DeleteAsync(It.IsAny<NoteKey>()))
            .ThrowsAsync(new ResourceNotFoundException());

        Func<Task> actual = async () => await _noteService.DeleteAsync(new NoteKey{Id = -1});

        await actual.Should().ThrowExactlyAsync<ResourceNotFoundException>();
        _repositoryMock.Verify(repository => repository.DeleteAsync(It.IsAny<NoteKey>()), Times.Once);
    }

    [Fact]
    public async Task Delete_NoteExist_NoteNoLongerExist()
    {
        _repositoryMock.Setup(repository => repository.DeleteAsync(It.IsAny<NoteKey>()));

        Func<Task> actual = async () => await _noteService.DeleteAsync(new NoteKey{Id = 1});

        await actual.Should().NotThrowAsync();
        _repositoryMock.Verify(repository => repository.DeleteAsync(It.IsAny<NoteKey>()), Times.Once);
    }
}