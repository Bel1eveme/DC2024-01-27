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

[TestSubject(typeof(IssueService))]
public class IssueServiceTest
{
    private readonly Mock<IMapper> _mapperMock = new();
    private readonly Mock<IIssueRepository<long>> _repositoryMock = new();
    private readonly Mock<AbstractValidator<Issue>> _validatorMock = new();
    private readonly Mock<ICacheService> _cacheServiceMock = new();
    private readonly IIssueService _issueService;

    public IssueServiceTest()
    {
        _issueService = new IssueService(_mapperMock.Object, _repositoryMock.Object, _validatorMock.Object,
            _cacheServiceMock.Object);
    }


    [Fact]
    public async Task CreateAsync_NullArgument_ThrowsArgumentNullException()
    {
        Func<Task> actual = async () => await _issueService.CreateAsync(null!);

        await actual.Should().ThrowExactlyAsync<ArgumentNullException>();
    }

    [Fact]
    public async Task CreateAsync_InvalidArgument_ThrowsValidationException()
    {
        IssueRequestDto issueRequestDto = new();
        Issue issue = new();
        ValidationResult validationResult =
            new ValidationResult(new List<ValidationFailure>([new ValidationFailure()]));
        _mapperMock.Setup(mapper => mapper.Map<Issue>(It.IsAny<IssueRequestDto>())).Returns(issue);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Issue>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);

        Func<Task> actual = async () => await _issueService.CreateAsync(issueRequestDto);

        await actual.Should().ThrowExactlyAsync<ValidationException>();
        _mapperMock.Verify(mapper => mapper.Map<Issue>(It.IsAny<IssueRequestDto>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Issue>>(), It.IsAny<CancellationToken>()),
            Times.Once);
        _cacheServiceMock.Verify(
            service => service.SetAsync(It.IsAny<string>(), It.IsAny<Issue>(), It.IsAny<CancellationToken>()),
            Times.Never);
    }

    [Fact]
    public async Task CreateAsync_ValidArgument_ReturnsAddedIssueResponseDto()
    {
        IssueRequestDto issueRequestDto = new();
        IssueResponseDto issueResponseDto = new();
        Issue issue = new();
        ValidationResult validationResult = new();
        _mapperMock.Setup(mapper => mapper.Map<Issue>(It.IsAny<IssueRequestDto>())).Returns(issue);
        _mapperMock.Setup(mapper => mapper.Map<IssueResponseDto>(It.IsAny<Issue>())).Returns(issueResponseDto);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Issue>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);
        _repositoryMock.Setup(repository => repository.AddAsync(It.IsAny<Issue>())).ReturnsAsync(issue);

        var result = await _issueService.CreateAsync(issueRequestDto);

        result.Should().Be(issueResponseDto);
        _mapperMock.Verify(mapper => mapper.Map<Issue>(It.IsAny<IssueRequestDto>()), Times.Once);
        _mapperMock.Verify(mapper => mapper.Map<IssueResponseDto>(It.IsAny<Issue>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Issue>>(), It.IsAny<CancellationToken>()),
            Times.Once);
        _repositoryMock.Verify(repository => repository.AddAsync(It.IsAny<Issue>()), Times.Once);
        _cacheServiceMock.Verify(
            service => service.SetAsync(It.IsAny<string>(), It.IsAny<Issue>(), It.IsAny<CancellationToken>()),
            Times.Once);
    }

    [Fact]
    public async Task GetByIdAsync_IssueNotExist_ThrowsResourceNotFoundException()
    {
        _cacheServiceMock
            .Setup(cacheService => cacheService.GetAsync(It.IsAny<string>(), It.IsAny<Func<Task<Issue>>>(),
                It.IsAny<CancellationToken>()))
            .ThrowsAsync(new ResourceNotFoundException());

        Func<Task> actual = async () => await _issueService.GetByIdAsync(-1);

        await actual.Should().ThrowExactlyAsync<ResourceNotFoundException>();
        _cacheServiceMock.Verify(cacheService => cacheService.GetAsync(It.IsAny<string>(),
            It.IsAny<Func<Task<Issue>>>(),
            It.IsAny<CancellationToken>()), Times.Once);
    }

    [Fact]
    public async Task GetByIdAsync_IssueExist_ReturnsExistingResult()
    {
        Issue issue = new Issue();
        IssueResponseDto issueResponseDto = new();
        _cacheServiceMock
            .Setup(cacheService => cacheService.GetAsync(It.IsAny<string>(), It.IsAny<Func<Task<Issue>>>(),
                It.IsAny<CancellationToken>()))
            .ReturnsAsync(issue);
        _mapperMock.Setup(mapper => mapper.Map<IssueResponseDto>(It.IsAny<Issue>())).Returns(issueResponseDto);

        var result = await _issueService.GetByIdAsync(1);

        result.Should().Be(issueResponseDto);
        _cacheServiceMock.Verify(cacheService => cacheService.GetAsync(It.IsAny<string>(),
            It.IsAny<Func<Task<Issue>>>(),
            It.IsAny<CancellationToken>()), Times.Once);
    }

    [Fact]
    public async Task GetAllAsync_EmptyRepository_ReturnsEmptyList()
    {
        _repositoryMock.Setup(repository => repository.GetAllAsync())
            .ReturnsAsync([]);

        var result = await _issueService.GetAllAsync();

        result.Should().BeEmpty();
        _repositoryMock.Verify(repository => repository.GetAllAsync(), Times.Once);
        _mapperMock.Verify(mapper => mapper.Map<IssueResponseDto>(It.IsAny<Issue>()), Times.Never);
        _cacheServiceMock.Verify(
            service => service.GetAsync<Issue>(It.IsAny<string>(), It.IsAny<CancellationToken>()), Times.Never);
    }

    [Fact]
    public async Task UpdateAsync_NullArgument_ThrowsArgumentNullException()
    {
        Func<Task> actual = async () => await _issueService.UpdateAsync(1, null!);

        await actual.Should().ThrowExactlyAsync<ArgumentNullException>();
    }

    [Fact]
    public async Task UpdateAsync_InvalidArgument_ThrowsValidationException()
    {
        IssueRequestDto issueRequestDto = new();
        Issue issue = new();
        ValidationResult validationResult =
            new ValidationResult(new List<ValidationFailure>([new ValidationFailure()]));
        _mapperMock.Setup(mapper => mapper.Map<Issue>(It.IsAny<IssueRequestDto>())).Returns(issue);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Issue>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);

        Func<Task> actual = async () => await _issueService.UpdateAsync(1, issueRequestDto);

        await actual.Should().ThrowExactlyAsync<ValidationException>();
        _mapperMock.Verify(mapper => mapper.Map<Issue>(It.IsAny<IssueRequestDto>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Issue>>(), It.IsAny<CancellationToken>()),
            Times.Once);
        _cacheServiceMock.Verify(
            service => service.SetAsync(It.IsAny<string>(), It.IsAny<Issue>(), It.IsAny<CancellationToken>()),
            Times.Never);
    }

    [Fact]
    public async Task UpdateAsync_ValidArgument_ReturnsAddedIssueResponseDto()
    {
        IssueRequestDto issueRequestDto = new();
        IssueResponseDto issueResponseDto = new();
        Issue issue = new();
        ValidationResult validationResult = new();
        _mapperMock.Setup(mapper => mapper.Map<Issue>(It.IsAny<IssueRequestDto>())).Returns(issue);
        _mapperMock.Setup(mapper => mapper.Map<IssueResponseDto>(It.IsAny<Issue>())).Returns(issueResponseDto);
        _validatorMock.Setup(validator =>
                validator.ValidateAsync(It.IsAny<ValidationContext<Issue>>(), It.IsAny<CancellationToken>()))
            .ReturnsAsync(validationResult);
        _repositoryMock.Setup(repository => repository.UpdateAsync(It.IsAny<long>(), It.IsAny<Issue>()))
            .ReturnsAsync(issue);

        var result = await _issueService.UpdateAsync(1, issueRequestDto);

        result.Should().Be(issueResponseDto);
        _mapperMock.Verify(mapper => mapper.Map<Issue>(It.IsAny<IssueRequestDto>()), Times.Once);
        _mapperMock.Verify(mapper => mapper.Map<IssueResponseDto>(It.IsAny<Issue>()), Times.Once);
        _validatorMock.Verify(
            validator => validator.ValidateAsync(It.IsAny<ValidationContext<Issue>>(), It.IsAny<CancellationToken>()),
            Times.Once);
        _repositoryMock.Verify(repository => repository.UpdateAsync(It.IsAny<long>(), It.IsAny<Issue>()), Times.Once);
        _cacheServiceMock.Verify(
            service => service.SetAsync(It.IsAny<string>(), It.IsAny<Issue>(), It.IsAny<CancellationToken>()),
            Times.Once);
    }

    [Fact]
    public async Task Delete_IssueNotExist_ThrowsResourceNotFoundException()
    {
        _repositoryMock.Setup(repository => repository.DeleteAsync(It.IsAny<long>()))
            .ThrowsAsync(new ResourceNotFoundException());

        Func<Task> actual = async () => await _issueService.DeleteAsync(-1);

        await actual.Should().ThrowExactlyAsync<ResourceNotFoundException>();
        _repositoryMock.Verify(repository => repository.DeleteAsync(It.IsAny<long>()), Times.Once);
        _cacheServiceMock.Verify(
            service => service.RemoveAsync(It.IsAny<string>(), It.IsAny<CancellationToken>()), Times.Never);
    }

    [Fact]
    public async Task Delete_IssueExist_IssueNoLongerExist()
    {
        _repositoryMock.Setup(repository => repository.DeleteAsync(It.IsAny<long>()));

        Func<Task> actual = async () => await _issueService.DeleteAsync(1);

        await actual.Should().NotThrowAsync();
        _repositoryMock.Verify(repository => repository.DeleteAsync(It.IsAny<long>()), Times.Once);
        _cacheServiceMock.Verify(
            service => service.RemoveAsync(It.IsAny<string>(), It.IsAny<CancellationToken>()), Times.Once);
    }
}