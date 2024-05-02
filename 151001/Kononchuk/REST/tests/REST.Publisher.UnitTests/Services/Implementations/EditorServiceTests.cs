using AutoMapper;
using FluentValidation;
using FluentAssertions;
using FluentValidation.Results;
using JetBrains.Annotations;
using Moq;
using REST.Publisher.Infrastructure.Redis.Interfaces;
using REST.Publisher.Models.DTOs.Request;
using REST.Publisher.Models.DTOs.Response;
using REST.Publisher.Models.Entities;
using REST.Publisher.Repositories.Interfaces;
using REST.Publisher.Services.Implementations;
using REST.Publisher.Services.Interfaces;
using REST.Publisher.Utilities.Exceptions;
using ValidationException = REST.Publisher.Utilities.Exceptions.ValidationException;


namespace REST.Publisher.UnitTests.Services.Implementations;

[TestSubject(typeof(EditorService))]
public class EditorServiceTests
{
    private readonly Mock<IMapper> _mapperMock = new();
    private readonly Mock<IEditorRepository<long>> _repositoryMock = new();
    private readonly Mock<AbstractValidator<Editor>> _validatorMock = new();
    private readonly Mock<ICacheService> _cacheServiceMock = new();
    private readonly IEditorService _editorService;

    public EditorServiceTests()
    {
        _editorService = new EditorService(_mapperMock.Object, _repositoryMock.Object, _validatorMock.Object,
            _cacheServiceMock.Object);
    }

    [Fact]
    public async Task CreateAsync_NullArgument_ThrowsArgumentNullException()
    {
        Func<Task> actual = async () => await _editorService.CreateAsync(null!);

        await actual.Should().ThrowExactlyAsync<ArgumentNullException>();
    }

    [Fact]
    public async Task CreateAsync_InvalidArgument_ThrowsValidationException()
    {
        EditorRequestDto editorRequestDto = new();
        Editor editor = new();
        ValidationResult validationResult =
            new ValidationResult(new List<ValidationFailure>([new ValidationFailure()]));
        _mapperMock.Setup(mapper => mapper.Map<Editor>(It.IsAny<EditorRequestDto>())).Returns(editor);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Editor>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);

        Func<Task> actual = async () => await _editorService.CreateAsync(editorRequestDto);

        await actual.Should().ThrowExactlyAsync<ValidationException>();
        _mapperMock.Verify(mapper => mapper.Map<Editor>(It.IsAny<EditorRequestDto>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Editor>>(), It.IsAny<CancellationToken>()),
            Times.Once);
        _cacheServiceMock.Verify(
            service => service.SetAsync(It.IsAny<string>(), It.IsAny<Editor>(), It.IsAny<CancellationToken>()),
            Times.Never);
    }

    [Fact]
    public async Task CreateAsync_ValidArgument_ReturnsAddedEditorResponseDto()
    {
        EditorRequestDto editorRequestDto = new();
        EditorResponseDto editorResponseDto = new();
        Editor editor = new();
        ValidationResult validationResult = new();
        _mapperMock.Setup(mapper => mapper.Map<Editor>(It.IsAny<EditorRequestDto>())).Returns(editor);
        _mapperMock.Setup(mapper => mapper.Map<EditorResponseDto>(It.IsAny<Editor>())).Returns(editorResponseDto);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Editor>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);
        _repositoryMock.Setup(repository => repository.AddAsync(It.IsAny<Editor>())).ReturnsAsync(editor);

        var result = await _editorService.CreateAsync(editorRequestDto);

        result.Should().Be(editorResponseDto);
        _mapperMock.Verify(mapper => mapper.Map<Editor>(It.IsAny<EditorRequestDto>()), Times.Once);
        _mapperMock.Verify(mapper => mapper.Map<EditorResponseDto>(It.IsAny<Editor>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Editor>>(), It.IsAny<CancellationToken>()),
            Times.Once);
        _repositoryMock.Verify(repository => repository.AddAsync(It.IsAny<Editor>()), Times.Once);
        _cacheServiceMock.Verify(
            service => service.SetAsync(It.IsAny<string>(), It.IsAny<Editor>(), It.IsAny<CancellationToken>()),
            Times.Once);
    }

    [Fact]
    public async Task GetByIdAsync_EditorNotExist_ThrowsResourceNotFoundException()
    {
        _cacheServiceMock
            .Setup(cacheService => cacheService.GetAsync(It.IsAny<string>(), It.IsAny<Func<Task<Editor>>>(),
                It.IsAny<CancellationToken>()))
            .ThrowsAsync(new ResourceNotFoundException());

        Func<Task> actual = async () => await _editorService.GetByIdAsync(-1);

        await actual.Should().ThrowExactlyAsync<ResourceNotFoundException>();
        _cacheServiceMock.Verify(cacheService => cacheService.GetAsync(It.IsAny<string>(),
            It.IsAny<Func<Task<Editor>>>(),
            It.IsAny<CancellationToken>()), Times.Once);
    }

    [Fact]
    public async Task GetByIdAsync_EditorExist_ReturnsExistingResult()
    {
        Editor editor = new Editor();
        EditorResponseDto editorResponseDto = new();
        _cacheServiceMock
            .Setup(cacheService => cacheService.GetAsync(It.IsAny<string>(), It.IsAny<Func<Task<Editor>>>(),
                It.IsAny<CancellationToken>()))
            .ReturnsAsync(editor);
        _mapperMock.Setup(mapper => mapper.Map<EditorResponseDto>(It.IsAny<Editor>())).Returns(editorResponseDto);

        var result = await _editorService.GetByIdAsync(1);

        result.Should().Be(editorResponseDto);
        _cacheServiceMock.Verify(cacheService => cacheService.GetAsync(It.IsAny<string>(),
            It.IsAny<Func<Task<Editor>>>(),
            It.IsAny<CancellationToken>()), Times.Once);
    }

    [Fact]
    public async Task GetAllAsync_EmptyRepository_ReturnsEmptyList()
    {
        _repositoryMock.Setup(repository => repository.GetAllAsync())
            .ReturnsAsync([]);
        
        var result = await _editorService.GetAllAsync();

        result.Should().BeEmpty();
        _repositoryMock.Verify(repository => repository.GetAllAsync(), Times.Once);
        _mapperMock.Verify(mapper => mapper.Map<EditorResponseDto>(It.IsAny<Editor>()), Times.Never);
        _cacheServiceMock.Verify(
            service => service.GetAsync<Editor>(It.IsAny<string>(), It.IsAny<CancellationToken>()), Times.Never);
    }

    [Fact]
    public async Task UpdateAsync_NullArgument_ThrowsArgumentNullException()
    {
        Func<Task> actual = async () => await _editorService.UpdateAsync(1, null!);

        await actual.Should().ThrowExactlyAsync<ArgumentNullException>();
    }

    [Fact]
    public async Task UpdateAsync_InvalidArgument_ThrowsValidationException()
    {
        EditorRequestDto editorRequestDto = new();
        Editor editor = new();
        ValidationResult validationResult =
            new ValidationResult(new List<ValidationFailure>([new ValidationFailure()]));
        _mapperMock.Setup(mapper => mapper.Map<Editor>(It.IsAny<EditorRequestDto>())).Returns(editor);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Editor>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);

        Func<Task> actual = async () => await _editorService.UpdateAsync(1, editorRequestDto);

        await actual.Should().ThrowExactlyAsync<ValidationException>();
        _mapperMock.Verify(mapper => mapper.Map<Editor>(It.IsAny<EditorRequestDto>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Editor>>(), It.IsAny<CancellationToken>()),
            Times.Once);
        _cacheServiceMock.Verify(
            service => service.SetAsync(It.IsAny<string>(), It.IsAny<Editor>(), It.IsAny<CancellationToken>()),
            Times.Never);
    }

    [Fact]
    public async Task UpdateAsync_ValidArgument_ReturnsAddedEditorResponseDto()
    {
        EditorRequestDto editorRequestDto = new();
        EditorResponseDto editorResponseDto = new();
        Editor editor = new();
        ValidationResult validationResult = new();
        _mapperMock.Setup(mapper => mapper.Map<Editor>(It.IsAny<EditorRequestDto>())).Returns(editor);
        _mapperMock.Setup(mapper => mapper.Map<EditorResponseDto>(It.IsAny<Editor>())).Returns(editorResponseDto);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Editor>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);
        _repositoryMock.Setup(repository => repository.UpdateAsync(It.IsAny<long>(), It.IsAny<Editor>()))
            .ReturnsAsync(editor);

        var result = await _editorService.UpdateAsync(1, editorRequestDto);

        result.Should().Be(editorResponseDto);
        _mapperMock.Verify(mapper => mapper.Map<Editor>(It.IsAny<EditorRequestDto>()), Times.Once);
        _mapperMock.Verify(mapper => mapper.Map<EditorResponseDto>(It.IsAny<Editor>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Editor>>(), It.IsAny<CancellationToken>()),
            Times.Once);
        _repositoryMock.Verify(repository => repository.UpdateAsync(It.IsAny<long>(), It.IsAny<Editor>()), Times.Once);
        _cacheServiceMock.Verify(
            service => service.SetAsync(It.IsAny<string>(), It.IsAny<Editor>(), It.IsAny<CancellationToken>()),
            Times.Once);
    }

    [Fact]
    public async Task Delete_EditorNotExist_ThrowsResourceNotFoundException()
    {
        _repositoryMock.Setup(repository => repository.DeleteAsync(It.IsAny<long>()))
            .ThrowsAsync(new ResourceNotFoundException());

        Func<Task> actual = async () => await _editorService.DeleteAsync(-1);

        await actual.Should().ThrowExactlyAsync<ResourceNotFoundException>();
        _repositoryMock.Verify(repository => repository.DeleteAsync(It.IsAny<long>()), Times.Once);
        _cacheServiceMock.Verify(
            service => service.RemoveAsync(It.IsAny<string>(), It.IsAny<CancellationToken>()), Times.Never);
    }

    [Fact]
    public async Task Delete_EditorExist_EditorNoLongerExist()
    {
        _repositoryMock.Setup(repository => repository.DeleteAsync(It.IsAny<long>()));

        Func<Task> actual = async () => await _editorService.DeleteAsync(1);

        await actual.Should().NotThrowAsync();
        _repositoryMock.Verify(repository => repository.DeleteAsync(It.IsAny<long>()), Times.Once);
        _cacheServiceMock.Verify(
            service => service.RemoveAsync(It.IsAny<string>(), It.IsAny<CancellationToken>()), Times.Once);
    }
}