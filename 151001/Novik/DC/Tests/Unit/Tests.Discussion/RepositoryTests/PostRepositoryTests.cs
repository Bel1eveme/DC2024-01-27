using Discussion.Models.Entity;
using Discussion.Repositories.interfaces;
using Moq;
using NUnit.Framework;
using Assert = NUnit.Framework.Assert;

namespace Tests.Discussion.RepositoryTests;

public class PostRepositoryTests
{
    [Test]
    public async Task AddAsync_ShouldAddEntity()
    {
        // Arrange
        var entity = new Post { id = 1, content = "Post 1" };
        var postRepoMock = new Mock<IPostRepository>();
        postRepoMock.Setup(repo => repo.AddAsync(entity)).ReturnsAsync(entity);
        // Act
        var result = await postRepoMock.Object.AddAsync(entity);

        // Assert
        Assert.That(result, Is.EqualTo(entity));
    }
    [Test]
    public async Task GetByIdAsync_ShouldReturnEntity()
    {
        // Arrange
        var entities = new List<Post> { new Post { id = 1, content = "Post 1" } };
        var postRepoMock = new Mock<IPostRepository>();
        postRepoMock.Setup(repo => repo.AddAsync(entities[0])).ReturnsAsync(entities[0]);
        // Act
        var result = await postRepoMock.Object.AddAsync(entities[0]);

        // Assert
        Assert.That(result, Is.EqualTo(entities[0]));
    }
    
    [Test]
    public async Task GetByIdAsync_EntityNotExists()
    {
        // Arrange
        var postRepoMock = new Mock<IPostRepository>();
        postRepoMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((Post)null);
        // Act
        var result = await postRepoMock.Object.GetByIdAsync(1);

        // Assert
        Assert.That(result, Is.EqualTo(null));
    }

    [Test]
    public async Task GetAllAsync_ShouldReturnAllEntities()
    {
        // Arrange
        var entities = new List<Post>
        {
            new Post { id = 1, content = "Post 1" },
            new Post { id = 2, content = "Post 2" }
        };
        var postRepoMock = new Mock<IPostRepository>();
        postRepoMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(entities);
        
        foreach (var entity in entities)
        {
            await postRepoMock.Object.AddAsync(entity);
        }

        // Act
        var result = await postRepoMock.Object.GetAllAsync();

        // Assert
        Assert.That(result.Count(), Is.EqualTo(2));
    }
    
    [Test]
    public async Task GetAllAsync_NoEntities()
    {
        // Arrange
        var postRepoMock = new Mock<IPostRepository>();
        var inserted = new Dictionary<long, Post>();
        postRepoMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(inserted.Values);
        // Act
        var result = await postRepoMock.Object.GetAllAsync();
        var expected = new Dictionary<long, Post>();
        // Assert
        Assert.That(result, Is.EqualTo(expected));
    }
    
    [Test]
    public async Task UpdateAsync_ShouldUpdateEntity()
    {
        // Arrange
        var entity = new Post { id = 1, content = "Updated Post" };
        var postRepoMock = new Mock<IPostRepository>();
        postRepoMock.Setup(repo => repo.UpdateAsync(entity)).ReturnsAsync(entity);
        
        // Act
        var result = await postRepoMock.Object.UpdateAsync(entity);

        // Assert
        Assert.That(result.content, Is.EqualTo("Updated Post"));
    }

    [Test]
    public async Task DeleteAsync_ShouldRemoveEntity()
    {
        var postRepoMock = new Mock<IPostRepository>();
        postRepoMock.Setup(repo => repo.DeleteAsync(1)).Returns(Task.CompletedTask);
        
        Task task =  postRepoMock.Object.DeleteAsync(1);

        // Assert
        // Verify that DeleteAsync method was called
        postRepoMock.Verify(repo => repo.DeleteAsync(1),Times.Once);
    }
    
    [Test]
    public async Task DeleteAsync_NoEntities()
    {
        var postRepoMock = new Mock<IPostRepository>();
        postRepoMock.Setup(repo => repo.DeleteAsync(1)).Returns(Task.CompletedTask);
        
        Task task =  postRepoMock.Object.DeleteAsync(1);

        // Assert
        // Verify that DeleteAsync method was called
        postRepoMock.Verify(repo => repo.DeleteAsync(1),Times.Once);
    }

    [Test]
    public async Task ExistsAsync_Entity_ExpectedTrue()
    {
        var postRepoMock = new Mock<IPostRepository>();
        postRepoMock.Setup(repo => repo.Exists(1)).ReturnsAsync(true);

        var result = await postRepoMock.Object.Exists(1);
        var expected = true;
        Assert.That(result,Is.EqualTo(expected));
    }
    
    [Test]
    public async Task ExistsAsync_NoEntity_ExpectedFalse()
    {
        var postRepoMock = new Mock<IPostRepository>();
        postRepoMock.Setup(repo => repo.Exists(1)).ReturnsAsync(false);

        var result = await postRepoMock.Object.Exists(1);
        var expected = false;
        Assert.That(result,Is.EqualTo(expected));
    }
}