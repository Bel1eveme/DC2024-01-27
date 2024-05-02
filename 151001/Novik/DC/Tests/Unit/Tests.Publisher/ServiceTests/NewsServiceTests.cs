using AutoMapper;
using Moq;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Models.Entity;
using Publisher.Repositories.interfaces;
using Publisher.Services;


namespace Tests.ServiceTests;

public class NewsServiceTests
{
    [Test]
    public async Task GetAllAsync_ShouldReturnAllNews()
    {
        // Arrange
        var newsEntities = new List<News>
        {
            new News { id = 1, title = "Label 1" },
            new News { id = 2, title = "Label 2" }
        };
        var newsResponseTos = new List<NewsResponseTo>
        {
            new NewsResponseTo() { id = 1, title = "Label 1" },
            new NewsResponseTo { id = 2, title = "Label 2" }
        };
        
        var newsRepositoryMock = new Mock<INewsRepository>();
        newsRepositoryMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(newsEntities);

        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<IEnumerable<NewsResponseTo>>(newsEntities)).Returns(newsResponseTos);

        var newsService = new NewsService(newsRepositoryMock.Object, mapperMock.Object);

        // Act
        var result = await newsService.GetAllAsync();

        // Assert
        Assert.NotNull(result);
        Assert.That(result.Count(), Is.EqualTo(2));
    }
    
    [Test]
    public async Task GetAllAsync_WhenNewsDontExist()
    {
        // Arrange
        var newsEntities = new List<News>();

        var newsResponseTo = new List<NewsResponseTo>();
        
        var newsRepositoryMock = new Mock<INewsRepository>();
        newsRepositoryMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(newsEntities);

        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<IEnumerable<NewsResponseTo>>(newsEntities)).Returns(newsResponseTo);

        var newsService = new NewsService(newsRepositoryMock.Object, mapperMock.Object);

        // Act
        var result = await newsService.GetAllAsync();

        // Assert
        Assert.That(result.Count(), Is.EqualTo(0));
    }
    
    [Test]
    public async Task AddAsync_ExistingEntity()
    {
        // Arrange
        var news = new News { id = 1, title = "Label 1" };

        var newsRequest = new NewsRequestTo() { id = 1, title = "Label 1" };

        var newsResponse = new NewsResponseTo { id = 1, title = "Label 1" };
        
        var newsRepositoryMock = new Mock<INewsRepository>();
        newsRepositoryMock.Setup(repo => repo.AddAsync(news)).ReturnsAsync(news);

        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<NewsResponseTo>(news)).Returns(newsResponse);
        mapperMock.Setup(mapper => mapper.Map<News>(newsRequest)).Returns(news);
        
        var newsService = new NewsService(newsRepositoryMock.Object, mapperMock.Object);

        // Act
        var result = await newsService.AddAsync(newsRequest);

        // Assert
        Assert.NotNull(result);
        Assert.That(result, Is.EqualTo(newsResponse));
    }

    [Test]
    public async Task DeleteAsync_ShouldDeleteNews_WhenNewsExists()
    {
        // Arrange
        var newsRepositoryMock = new Mock<INewsRepository>();
        newsRepositoryMock.Setup(repo => repo.Exists(1)).ReturnsAsync(true);

        var mapperMock = new Mock<IMapper>();

        var newsService = new NewsService(newsRepositoryMock.Object, mapperMock.Object);

        // Act
        await newsService.DeleteAsync(1);

        // Assert
        newsRepositoryMock.Verify(repo => repo.DeleteAsync(1), Times.Once);
    }

    [Test]
    public async Task DeleteAsync_ShouldThrowException_WhenNewsDoesNotExist()
    {
        // Arrange
        var newsRepositoryMock = new Mock<INewsRepository>();
        newsRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((News)null);

        var mapperMock = new Mock<IMapper>();

        var newsService = new NewsService(newsRepositoryMock.Object, mapperMock.Object);

        // Act & Assert
        Assert.ThrowsAsync<KeyNotFoundException>(() => newsService.DeleteAsync(1));
    }
    [Test]
    public async Task UpdateAsync_ShouldReturnNews_WhenNewsExists()
    {
        // Arrange
        var newsRequest = new NewsRequestTo { id = 1, title = "Updated Label" };
        var news = new News { id = 1, title = "Updated Label" };
        var newsResponse = new NewsResponseTo { id = 1, title = "Updated Label" };
        
        var newsRepositoryMock = new Mock<INewsRepository>();
        newsRepositoryMock.Setup(repo => repo.Exists(1)).ReturnsAsync(true);
        newsRepositoryMock.Setup(repo => repo.UpdateAsync(news)).ReturnsAsync(news);
        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<NewsResponseTo>(news)).Returns(newsResponse);
        mapperMock.Setup(mapper => mapper.Map<News>(newsRequest)).Returns(news);
        var newsService = new NewsService(newsRepositoryMock.Object, mapperMock.Object);

        var result =await newsService.UpdateAsync(newsRequest);
        Xunit.Assert.Equal(newsResponse, result);
    }
    [Test]
    public async Task UpdateAsync_ShouldThrowException_WhenNewsDoesNotExist()
    {
        // Arrange
        var newsRequest = new NewsRequestTo { id = 1, title = "Updated Label" };

        var newsRepositoryMock = new Mock<INewsRepository>();
        newsRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((News)null);

        var mapperMock = new Mock<IMapper>();

        var newsService = new NewsService(newsRepositoryMock.Object, mapperMock.Object);

        // Act & Assert
        Assert.ThrowsAsync<KeyNotFoundException>(() => newsService.UpdateAsync(newsRequest));
    }
    
    [Test]
    public async Task GetById_ShouldReturnNews_WhenNewsExists()
    {
        var news = new News() { id = 1, title = "Label 1" };
        var newsResponse = new NewsResponseTo() { id = 1, title = "Label 1" };
        // Arrange
        var newsRepositoryMock = new Mock<INewsRepository>();
        newsRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync(news);

        var mapperMock = new Mock<IMapper>();
        
        var newsService = new NewsService(newsRepositoryMock.Object, mapperMock.Object);
        mapperMock.Setup(mapper => mapper.Map<NewsResponseTo>(news)).Returns(newsResponse);
        // Act
        var result = await newsService.GetByIdAsync(1);

        // Assert
        Assert.That(result, Is.EqualTo(newsResponse));
    }

    [Test]
    public async Task GetById_ShouldThrowException_WhenNewsDoesNotExist()
    {
        // Arrange
        var newsRepositoryMock = new Mock<INewsRepository>();
        newsRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((News)null);

        var mapperMock = new Mock<IMapper>();

        var newsService = new NewsService(newsRepositoryMock.Object, mapperMock.Object);

        // Act & Assert
        Assert.ThrowsAsync<KeyNotFoundException>(() => newsService.GetByIdAsync(1));
    }
}