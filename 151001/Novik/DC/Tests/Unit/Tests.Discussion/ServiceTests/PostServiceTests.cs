using AutoMapper;
using Discussion.Models.DTOs.Requests;
using Discussion.Models.DTOs.Responses;
using Discussion.Models.Entity;
using Discussion.Repositories.interfaces;
using Discussion.Services;
using Moq;
using NUnit.Framework;
using Assert = NUnit.Framework.Assert;

namespace Tests.Discussion.ServiceTests;

public class PostServiceTests
{
    [Test]
    public async Task GetAllAsync_ShouldReturnAllPosts()
    {
        // Arrange
        var postEntities = new List<Post>
        {
            new Post { id = 1, content = "Label 1" },
            new Post { id = 2, content = "Label 2" }
        };
        var postResponseTo = new List<PostResponseTo>
        {
            new PostResponseTo { id = 1, content = "Label 1" },
            new PostResponseTo { id = 2, content = "Label 2" }
        };
        
        var postRepositoryMock = new Mock<IPostRepository>();
        postRepositoryMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(postEntities);

        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<IEnumerable<PostResponseTo>>(postEntities)).Returns(postResponseTo);

        var postService = new PostService(postRepositoryMock.Object, mapperMock.Object);

        // Act
        var result = await postService.GetAllAsync();

        // Assert
        Assert.That(result.Count(), Is.EqualTo(2));
    }
    
    [Test]
    public async Task GetAllAsync_WhenPostsDontExist()
    {
        // Arrange
        var postEntities = new List<Post>();

        var postResponseTo = new List<PostResponseTo>();
        
        var postRepositoryMock = new Mock<IPostRepository>();
        postRepositoryMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(postEntities);

        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<IEnumerable<PostResponseTo>>(postEntities)).Returns(postResponseTo);

        var postService = new PostService(postRepositoryMock.Object, mapperMock.Object);

        // Act
        var result = await postService.GetAllAsync();

        // Assert
        Assert.That(result.Count(), Is.EqualTo(0));
    }
    
    [Test]
    public async Task AddAsync_ExistingEntity()
    {
        // Arrange
        var post = new Post { id = 1, content = "Label 1" };

        var postRequest = new PostRequestTo() { id = 1, content = "Label 1" };

        var postResponse = new PostResponseTo { id = 1, content = "Label 1" };
        
        var postRepositoryMock = new Mock<IPostRepository>();
        postRepositoryMock.Setup(repo => repo.AddAsync(post)).ReturnsAsync(post);

        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<PostResponseTo>(post)).Returns(postResponse);
        mapperMock.Setup(mapper => mapper.Map<Post>(postRequest)).Returns(post);
        
        var postService = new PostService(postRepositoryMock.Object, mapperMock.Object);

        // Act
        var result = await postService.AddAsync(postRequest, "ru");

        // Assert
        Assert.That(result, Is.EqualTo(postResponse));
    }

    [Test]
    public async Task DeleteAsync_ShouldDeleteLabel_WhenPostExists()
    {
        // Arrange
        var postRepositoryMock = new Mock<IPostRepository>();
        postRepositoryMock.Setup(repo => repo.Exists(1)).ReturnsAsync(true);

        var mapperMock = new Mock<IMapper>();

        var postService = new PostService(postRepositoryMock.Object, mapperMock.Object);

        // Act
        await postService.DeleteAsync(1);

        // Assert
        postRepositoryMock.Verify(repo => repo.DeleteAsync(1), Times.Once);
    }

    [Test]
    public async Task DeleteAsync_ShouldThrowException_WhenPostDoesNotExist()
    {
        // Arrange
        var postRepositoryMock = new Mock<IPostRepository>();
        postRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((Post)null);

        var mapperMock = new Mock<IMapper>();

        var postService = new PostService(postRepositoryMock.Object, mapperMock.Object);

        // Act & Assert
        Assert.ThrowsAsync<KeyNotFoundException>(() => postService.DeleteAsync(1));
    }
    [Test]
    public async Task UpdateAsync_ShouldReturnPost_WhenPostExists()
    {
        // Arrange
        var postRequest = new PostRequestTo { id = 1, content = "Updated Label" };
        var post = new Post { id = 1, content = "Updated Label" };
        var postResponse = new PostResponseTo { id = 1, content = "Updated Label" };
        
        var postRepositoryMock = new Mock<IPostRepository>();
        postRepositoryMock.Setup(repo => repo.Exists(1)).ReturnsAsync(true);
        postRepositoryMock.Setup(repo => repo.UpdateAsync(post)).ReturnsAsync(post);
        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<PostResponseTo>(post)).Returns(postResponse);
        mapperMock.Setup(mapper => mapper.Map<Post>(postRequest)).Returns(post);
        var labelService = new PostService(postRepositoryMock.Object, mapperMock.Object);

        var result =await labelService.UpdateAsync(postRequest, "ru");
        Xunit.Assert.Equal(postResponse, result);
    }
    [Test]
    public async Task UpdateAsync_ShouldThrowException_WhenPostDoesNotExist()
    {
        // Arrange
        var postRequest = new PostRequestTo { id = 1, content = "Updated Label" };

        var postRepositoryMock = new Mock<IPostRepository>();
        postRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((Post)null);

        var mapperMock = new Mock<IMapper>();

        var postService = new PostService(postRepositoryMock.Object, mapperMock.Object);

        // Act & Assert
        Assert.ThrowsAsync<KeyNotFoundException>(() => postService.UpdateAsync(postRequest, "ru"));
    }
    
    [Test]
    public async Task GetById_ShouldReturnPost_WhenLabelExists()
    {
        var post = new Post() { id = 1, content = "Label 1" };
        var postResponse = new PostResponseTo() { id = 1, content = "Label 1" };
        // Arrange
        var postRepositoryMock = new Mock<IPostRepository>();
        postRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync(post);

        var mapperMock = new Mock<IMapper>();
        
        var postService = new PostService(postRepositoryMock.Object, mapperMock.Object);
        mapperMock.Setup(mapper => mapper.Map<PostResponseTo>(post)).Returns(postResponse);
        // Act
        var result = await postService.GetByIdAsync(1);

        // Assert
        Assert.That(result, Is.EqualTo(postResponse));
    }

    [Test]
    public async Task GetById_ShouldThrowException_WhenPostDoesNotExist()
    {
        // Arrange
        var postRepositoryMock = new Mock<IPostRepository>();
        postRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((Post)null);

        var mapperMock = new Mock<IMapper>();

        var postService = new PostService(postRepositoryMock.Object, mapperMock.Object);

        // Act & Assert
        Assert.ThrowsAsync<KeyNotFoundException>(() => postService.GetByIdAsync(1));
    }
}