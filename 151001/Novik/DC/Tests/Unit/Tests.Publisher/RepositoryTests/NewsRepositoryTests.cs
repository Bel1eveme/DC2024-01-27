using System.Collections;
using Microsoft.AspNetCore.Mvc;
using Moq;
using Publisher.Models.Entity;
using Publisher.Repositories.interfaces;


namespace Tests.RepositoryTests;

public class NewsRepositoryTests
{

    [Test]
    public async Task AddAsync_ShouldAddEntity()
    {
        // Arrange
        var entity = new News { id = 1, title = "News 1" };
        var newsRepoMock = new Mock<INewsRepository>();
        newsRepoMock.Setup(repo => repo.AddAsync(entity)).ReturnsAsync(entity);
        // Act
        var result = await newsRepoMock.Object.AddAsync(entity);

        // Assert
        Assert.NotNull(result);
        Assert.That(result, Is.EqualTo(entity));
    }
    [Test]
    public async Task GetByIdAsync_ShouldReturnEntity()
    {
        // Arrange
        var entities = new List<News> { new News { id = 1, title = "News 1" } };
        var newsRepoMock = new Mock<INewsRepository>();
        newsRepoMock.Setup(repo => repo.AddAsync(entities[0])).ReturnsAsync(entities[0]);
        // Act
        var result = await newsRepoMock.Object.AddAsync(entities[0]);

        // Assert
        Assert.NotNull(result);
        Assert.That(result, Is.EqualTo(entities[0]));
    }
    
    [Test]
    public async Task GetByIdAsync_EntityNotExists()
    {
        // Arrange
        var newsRepoMock = new Mock<INewsRepository>();
        newsRepoMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((News)null);
        // Act
        var result = await newsRepoMock.Object.GetByIdAsync(1);

        // Assert
        Assert.That(result, Is.EqualTo(null));
    }

    [Test]
    public async Task GetAllAsync_ShouldReturnAllEntities()
    {
        // Arrange
        var entities = new List<News>
        {
            new News { id = 1, title = "News 1" },
            new News { id = 2, title = "News 2" }
        };
        var newsRepoMock = new Mock<INewsRepository>();
        newsRepoMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(entities);
        
        foreach (var entity in entities)
        {
            await newsRepoMock.Object.AddAsync(entity);
        }

        // Act
        var result = await newsRepoMock.Object.GetAllAsync();

        // Assert
        Assert.NotNull(result);
        Assert.That(result.Count(), Is.EqualTo(2));
    }
    
    [Test]
    public async Task GetAllAsync_NoEntities()
    {
        // Arrange
        var newsRepoMock = new Mock<INewsRepository>();
        var inserted = new Dictionary<long, News>();
        newsRepoMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(inserted.Values);
        // Act
        var result = await newsRepoMock.Object.GetAllAsync();
        var expected = new Dictionary<long, News>();
        // Assert
        Assert.That(result, Is.EqualTo(expected));
    }
    
    [Test]
    public async Task UpdateAsync_ShouldUpdateEntity()
    {
        // Arrange
        var entity = new News { id = 1, title = "Updated News" };
        var newsRepoMock = new Mock<INewsRepository>();
        newsRepoMock.Setup(repo => repo.UpdateAsync(entity)).ReturnsAsync(entity);
        
        // Act
        var result = await newsRepoMock.Object.UpdateAsync(entity);

        // Assert
        Assert.NotNull(result);
        Assert.That(result.title, Is.EqualTo("Updated News"));
    }

    [Test]
    public async Task DeleteAsync_ShouldRemoveEntity()
    {
        var newsRepoMock = new Mock<INewsRepository>();
        newsRepoMock.Setup(repo => repo.DeleteAsync(1)).Returns(Task.CompletedTask);
        
        Task task =  newsRepoMock.Object.DeleteAsync(1);

        // Assert
        // Verify that DeleteAsync method was called
        newsRepoMock.Verify(repo => repo.DeleteAsync(1),Times.Once);
    }
    
    [Test]
    public async Task DeleteAsync_NoEntities()
    {
        var newsRepoMock = new Mock<INewsRepository>();
        newsRepoMock.Setup(repo => repo.DeleteAsync(1)).Returns(Task.CompletedTask);
        
        Task task =  newsRepoMock.Object.DeleteAsync(1);

        // Assert
        // Verify that DeleteAsync method was called
        newsRepoMock.Verify(repo => repo.DeleteAsync(1),Times.Once);
    }

    [Test]
    public async Task ExistsAsync_Entity_ExpectedTrue()
    {
        var newsRepoMock = new Mock<INewsRepository>();
        newsRepoMock.Setup(repo => repo.Exists(1)).ReturnsAsync(true);

        var result = await newsRepoMock.Object.Exists(1);
        var expected = true;
        Assert.That(result,Is.EqualTo(expected));
    }
    
    [Test]
    public async Task ExistsAsync_NoEntity_ExpectedFalse()
    {
        var newsRepoMock = new Mock<INewsRepository>();
        newsRepoMock.Setup(repo => repo.Exists(1)).ReturnsAsync(false);

        var result = await newsRepoMock.Object.Exists(1);
        var expected = false;
        Assert.That(result,Is.EqualTo(expected));
    }
    
}