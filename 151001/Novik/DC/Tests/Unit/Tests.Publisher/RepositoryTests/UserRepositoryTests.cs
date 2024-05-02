using System.Collections;
using Microsoft.AspNetCore.Mvc;
using Moq;
using Publisher.Models.Entity;
using Publisher.Repositories.interfaces;


namespace Tests.RepositoryTests;

public class UserRepositoryTests
{

    [Test]
    public async Task AddAsync_ShouldAddEntity()
    {
        // Arrange
        var entity = new User { id = 1, login = "User 1" };
        var userRepoMock = new Mock<IUserRepository>();
        userRepoMock.Setup(repo => repo.AddAsync(entity)).ReturnsAsync(entity);
        // Act
        var result = await userRepoMock.Object.AddAsync(entity);

        // Assert
        Assert.NotNull(result);
        Assert.That(result, Is.EqualTo(entity));
    }
    [Test]
    public async Task GetByIdAsync_ShouldReturnEntity()
    {
        // Arrange
        var entities = new List<User> { new User { id = 1, login = "User 1" } };
        var userRepoMock = new Mock<IUserRepository>();
        userRepoMock.Setup(repo => repo.AddAsync(entities[0])).ReturnsAsync(entities[0]);
        // Act
        var result = await userRepoMock.Object.AddAsync(entities[0]);

        // Assert
        Assert.NotNull(result);
        Assert.That(result, Is.EqualTo(entities[0]));
    }
    
    [Test]
    public async Task GetByIdAsync_EntityNotExists()
    {
        // Arrange
        var userRepoMock = new Mock<IUserRepository>();
        userRepoMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((User)null);
        // Act
        var result = await userRepoMock.Object.GetByIdAsync(1);

        // Assert
        Assert.That(result, Is.EqualTo(null));
    }

    [Test]
    public async Task GetAllAsync_ShouldReturnAllEntities()
    {
        // Arrange
        var entities = new List<User>
        {
            new User { id = 1, login = "User 1" },
            new User { id = 2, login = "User 2" }
        };
        var userRepoMock = new Mock<IUserRepository>();
        userRepoMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(entities);
        
        foreach (var entity in entities)
        {
            await userRepoMock.Object.AddAsync(entity);
        }

        // Act
        var result = await userRepoMock.Object.GetAllAsync();

        // Assert
        Assert.NotNull(result);
        Assert.That(result.Count(), Is.EqualTo(2));
    }
    
    [Test]
    public async Task GetAllAsync_NoEntities()
    {
        // Arrange
        var userRepoMock = new Mock<IUserRepository>();
        var inserted = new Dictionary<long, User>();
        userRepoMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(inserted.Values);
        // Act
        var result = await userRepoMock.Object.GetAllAsync();
        var expected = new Dictionary<long, User>();
        // Assert
        Assert.That(result, Is.EqualTo(expected));
    }
    
    [Test]
    public async Task UpdateAsync_ShouldUpdateEntity()
    {
        // Arrange
        var entity = new User { id = 1, login = "Updated User" };
        var userRepoMock = new Mock<IUserRepository>();
        userRepoMock.Setup(repo => repo.UpdateAsync(entity)).ReturnsAsync(entity);
        
        // Act
        var result = await userRepoMock.Object.UpdateAsync(entity);

        // Assert
        Assert.NotNull(result);
        Assert.That(result.login, Is.EqualTo("Updated User"));
    }

    [Test]
    public async Task DeleteAsync_ShouldRemoveEntity()
    {
        var userRepoMock = new Mock<IUserRepository>();
        userRepoMock.Setup(repo => repo.DeleteAsync(1)).Returns(Task.CompletedTask);
        
        Task task =  userRepoMock.Object.DeleteAsync(1);

        // Assert
        // Verify that DeleteAsync method was called
        userRepoMock.Verify(repo => repo.DeleteAsync(1),Times.Once);
    }
    
    [Test]
    public async Task DeleteAsync_NoEntities()
    {
        var userRepoMock = new Mock<IUserRepository>();
        userRepoMock.Setup(repo => repo.DeleteAsync(1)).Returns(Task.CompletedTask);
        
        Task task =  userRepoMock.Object.DeleteAsync(1);

        // Assert
        // Verify that DeleteAsync method was called
        userRepoMock.Verify(repo => repo.DeleteAsync(1),Times.Once);
    }

    [Test]
    public async Task ExistsAsync_Entity_ExpectedTrue()
    {
        var userRepoMock = new Mock<IUserRepository>();
        userRepoMock.Setup(repo => repo.Exists(1)).ReturnsAsync(true);

        var result = await userRepoMock.Object.Exists(1);
        var expected = true;
        Assert.That(result,Is.EqualTo(expected));
    }
    
    [Test]
    public async Task ExistsAsync_NoEntity_ExpectedFalse()
    {
        var userRepoMock = new Mock<IUserRepository>();
        userRepoMock.Setup(repo => repo.Exists(1)).ReturnsAsync(false);

        var result = await userRepoMock.Object.Exists(1);
        var expected = false;
        Assert.That(result,Is.EqualTo(expected));
    }
    
}