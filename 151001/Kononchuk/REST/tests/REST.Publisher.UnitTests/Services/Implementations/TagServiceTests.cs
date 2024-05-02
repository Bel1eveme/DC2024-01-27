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

[TestSubject(typeof(TagService))]
public class TagServiceTest
{
    private readonly Mock<IMapper> _mapperMock = new();
    private readonly Mock<ITagRepository<long>> _repositoryMock = new();
    private readonly Mock<AbstractValidator<Tag>> _validatorMock = new();
    private readonly Mock<ICacheService> _cacheServiceMock = new();
    private readonly ITagService _tagService;

    public TagServiceTest()
    {
        _tagService = new TagService(_mapperMock.Object, _repositoryMock.Object, _validatorMock.Object,
            _cacheServiceMock.Object);
    }


    [Fact]
    public async Task CreateAsync_NullArgument_ThrowsArgumentNullException()
    {
        Func<Task> actual = async () => await _tagService.CreateAsync(null!);

        await actual.Should().ThrowExactlyAsync<ArgumentNullException>();
    }

    [Fact]
    public async Task CreateAsync_InvalidArgument_ThrowsValidationException()
    {
        TagRequestDto tagRequestDto = new();
        Tag tag = new();
        ValidationResult validationResult =
            new ValidationResult(new List<ValidationFailure>([new ValidationFailure()]));
        _mapperMock.Setup(mapper => mapper.Map<Tag>(It.IsAny<TagRequestDto>())).Returns(tag);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Tag>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);

        Func<Task> actual = async () => await _tagService.CreateAsync(tagRequestDto);

        await actual.Should().ThrowExactlyAsync<ValidationException>();
        _mapperMock.Verify(mapper => mapper.Map<Tag>(It.IsAny<TagRequestDto>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Tag>>(), It.IsAny<CancellationToken>()),
            Times.Once);
        _cacheServiceMock.Verify(
            service => service.SetAsync(It.IsAny<string>(), It.IsAny<Tag>(), It.IsAny<CancellationToken>()),
            Times.Never);
    }

    [Fact]
    public async Task CreateAsync_ValidArgument_ReturnsAddedTagResponseDto()
    {
        TagRequestDto tagRequestDto = new();
        TagResponseDto tagResponseDto = new();
        Tag tag = new();
        ValidationResult validationResult = new();
        _mapperMock.Setup(mapper => mapper.Map<Tag>(It.IsAny<TagRequestDto>())).Returns(tag);
        _mapperMock.Setup(mapper => mapper.Map<TagResponseDto>(It.IsAny<Tag>())).Returns(tagResponseDto);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Tag>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);
        _repositoryMock.Setup(repository => repository.AddAsync(It.IsAny<Tag>())).ReturnsAsync(tag);

        var result = await _tagService.CreateAsync(tagRequestDto);

        result.Should().Be(tagResponseDto);
        _mapperMock.Verify(mapper => mapper.Map<Tag>(It.IsAny<TagRequestDto>()), Times.Once);
        _mapperMock.Verify(mapper => mapper.Map<TagResponseDto>(It.IsAny<Tag>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Tag>>(), It.IsAny<CancellationToken>()),
            Times.Once);
        _repositoryMock.Verify(repository => repository.AddAsync(It.IsAny<Tag>()), Times.Once);
        _cacheServiceMock.Verify(
            service => service.SetAsync(It.IsAny<string>(), It.IsAny<Tag>(), It.IsAny<CancellationToken>()),
            Times.Once);
    }

    [Fact]
    public async Task GetByIdAsync_TagNotExist_ThrowsResourceNotFoundException()
    {
        _cacheServiceMock
            .Setup(cacheService => cacheService.GetAsync(It.IsAny<string>(), It.IsAny<Func<Task<Tag>>>(),
                It.IsAny<CancellationToken>()))
            .ThrowsAsync(new ResourceNotFoundException());

        Func<Task> actual = async () => await _tagService.GetByIdAsync(-1);

        await actual.Should().ThrowExactlyAsync<ResourceNotFoundException>();
        _cacheServiceMock.Verify(cacheService => cacheService.GetAsync(It.IsAny<string>(),
            It.IsAny<Func<Task<Tag>>>(),
            It.IsAny<CancellationToken>()), Times.Once);
    }

    [Fact]
    public async Task GetByIdAsync_TagExist_ReturnsExistingResult()
    {
        Tag tag = new Tag();
        TagResponseDto tagResponseDto = new();
        _cacheServiceMock
            .Setup(cacheService => cacheService.GetAsync(It.IsAny<string>(), It.IsAny<Func<Task<Tag>>>(),
                It.IsAny<CancellationToken>()))
            .ReturnsAsync(tag);
        _mapperMock.Setup(mapper => mapper.Map<TagResponseDto>(It.IsAny<Tag>())).Returns(tagResponseDto);

        var result = await _tagService.GetByIdAsync(1);

        result.Should().Be(tagResponseDto);
        _cacheServiceMock.Verify(cacheService => cacheService.GetAsync(It.IsAny<string>(),
            It.IsAny<Func<Task<Tag>>>(),
            It.IsAny<CancellationToken>()), Times.Once);
    }

    [Fact]
    public async Task GetAllAsync_EmptyRepository_ReturnsEmptyList()
    {
        _repositoryMock.Setup(repository => repository.GetAllAsync())
            .ReturnsAsync([]);

        var result = await _tagService.GetAllAsync();

        result.Should().BeEmpty();
        _repositoryMock.Verify(repository => repository.GetAllAsync(), Times.Once);
        _mapperMock.Verify(mapper => mapper.Map<TagResponseDto>(It.IsAny<Tag>()), Times.Never);
        _cacheServiceMock.Verify(
            service => service.GetAsync<Tag>(It.IsAny<string>(), It.IsAny<CancellationToken>()), Times.Never);
    }

    [Fact]
    public async Task UpdateAsync_NullArgument_ThrowsArgumentNullException()
    {
        Func<Task> actual = async () => await _tagService.UpdateAsync(1, null!);

        await actual.Should().ThrowExactlyAsync<ArgumentNullException>();
    }

    [Fact]
    public async Task UpdateAsync_InvalidArgument_ThrowsValidationException()
    {
        TagRequestDto tagRequestDto = new();
        Tag tag = new();
        ValidationResult validationResult =
            new ValidationResult(new List<ValidationFailure>([new ValidationFailure()]));
        _mapperMock.Setup(mapper => mapper.Map<Tag>(It.IsAny<TagRequestDto>())).Returns(tag);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Tag>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);

        Func<Task> actual = async () => await _tagService.UpdateAsync(1, tagRequestDto);

        await actual.Should().ThrowExactlyAsync<ValidationException>();
        _mapperMock.Verify(mapper => mapper.Map<Tag>(It.IsAny<TagRequestDto>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Tag>>(), It.IsAny<CancellationToken>()),
            Times.Once);
        _cacheServiceMock.Verify(
            service => service.SetAsync(It.IsAny<string>(), It.IsAny<Tag>(), It.IsAny<CancellationToken>()),
            Times.Never);
    }

    [Fact]
    public async Task UpdateAsync_ValidArgument_ReturnsAddedTagResponseDto()
    {
        TagRequestDto tagRequestDto = new();
        TagResponseDto tagResponseDto = new();
        Tag tag = new();
        ValidationResult validationResult = new();
        _mapperMock.Setup(mapper => mapper.Map<Tag>(It.IsAny<TagRequestDto>())).Returns(tag);
        _mapperMock.Setup(mapper => mapper.Map<TagResponseDto>(It.IsAny<Tag>())).Returns(tagResponseDto);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Tag>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);
        _repositoryMock.Setup(repository => repository.UpdateAsync(It.IsAny<long>(), It.IsAny<Tag>()))
            .ReturnsAsync(tag);

        var result = await _tagService.UpdateAsync(1, tagRequestDto);

        result.Should().Be(tagResponseDto);
        _mapperMock.Verify(mapper => mapper.Map<Tag>(It.IsAny<TagRequestDto>()), Times.Once);
        _mapperMock.Verify(mapper => mapper.Map<TagResponseDto>(It.IsAny<Tag>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Tag>>(), It.IsAny<CancellationToken>()),
            Times.Once);
        _repositoryMock.Verify(repository => repository.UpdateAsync(It.IsAny<long>(), It.IsAny<Tag>()), Times.Once);
        _cacheServiceMock.Verify(
            service => service.SetAsync(It.IsAny<string>(), It.IsAny<Tag>(), It.IsAny<CancellationToken>()),
            Times.Once);
    }

    [Fact]
    public async Task Delete_TagNotExist_ThrowsResourceNotFoundException()
    {
        _repositoryMock.Setup(repository => repository.DeleteAsync(It.IsAny<long>()))
            .ThrowsAsync(new ResourceNotFoundException());

        Func<Task> actual = async () => await _tagService.DeleteAsync(-1);

        await actual.Should().ThrowExactlyAsync<ResourceNotFoundException>();
        _repositoryMock.Verify(repository => repository.DeleteAsync(It.IsAny<long>()), Times.Once);
        _cacheServiceMock.Verify(
            service => service.RemoveAsync(It.IsAny<string>(), It.IsAny<CancellationToken>()), Times.Never);
    }

    [Fact]
    public async Task Delete_TagExist_TagNoLongerExist()
    {
        _repositoryMock.Setup(repository => repository.DeleteAsync(It.IsAny<long>()));

        Func<Task> actual = async () => await _tagService.DeleteAsync(1);

        await actual.Should().NotThrowAsync();
        _repositoryMock.Verify(repository => repository.DeleteAsync(It.IsAny<long>()), Times.Once);
        _cacheServiceMock.Verify(
            service => service.RemoveAsync(It.IsAny<string>(), It.IsAny<CancellationToken>()), Times.Once);
    }
}