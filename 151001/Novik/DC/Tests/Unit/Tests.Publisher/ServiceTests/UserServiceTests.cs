using AutoMapper;
using Moq;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Models.Entity;
using Publisher.Repositories.interfaces;
using Publisher.Services;


namespace Tests.ServiceTests;

public class UserServiceTests
{
    [Test]
    public async Task GetAllAsync_ShouldReturnAllUsers()
    {
        // Arrange
        var userEntities = new List<User>
        {
            new User { id = 1, firstname = "Label 1" },
            new User { id = 2, firstname = "Label 2" }
        };
        var userResponseTo = new List<UserResponseTo>
        {
            new UserResponseTo { id = 1, firstname = "Label 1" },
            new UserResponseTo { id = 2, firstname = "Label 2" }
        };
        
        var userRepositoryMock = new Mock<IUserRepository>();
        userRepositoryMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(userEntities);

        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<IEnumerable<UserResponseTo>>(userEntities)).Returns(userResponseTo);

        var userService = new UserService(userRepositoryMock.Object, mapperMock.Object);

        // Act
        var result = await userService.GetAllAsync();

        // Assert
        Assert.NotNull(result);
        Assert.That(result.Count(), Is.EqualTo(2));
    }
    
    [Test]
    public async Task GetAllAsync_WhenUsersDontExist()
    {
        // Arrange
        var userEntities = new List<User>();

        var userResponseTo = new List<UserResponseTo>();
        
        var userRepositoryMock = new Mock<IUserRepository>();
        userRepositoryMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(userEntities);

        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<IEnumerable<UserResponseTo>>(userEntities)).Returns(userResponseTo);

        var userService = new UserService(userRepositoryMock.Object, mapperMock.Object);

        // Act
        var result = await userService.GetAllAsync();

        // Assert
        Assert.That(result.Count(), Is.EqualTo(0));
    }
    
    [Test]
    public async Task AddAsync_ExistingEntity()
    {
        // Arrange
        var user = new User { id = 1, firstname = "Label 1" };

        var userRequest = new UserRequestTo() { id = 1, firstname = "Label 1" };

        var userResponse = new UserResponseTo { id = 1, firstname = "Label 1" };
        
        var userRepositoryMock = new Mock<IUserRepository>();
        userRepositoryMock.Setup(repo => repo.AddAsync(user)).ReturnsAsync(user);

        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<UserResponseTo>(user)).Returns(userResponse);
        mapperMock.Setup(mapper => mapper.Map<User>(userRequest)).Returns(user);
        
        var userService = new UserService(userRepositoryMock.Object, mapperMock.Object);

        // Act
        var result = await userService.AddAsync(userRequest);

        // Assert
        Assert.NotNull(result);
        Assert.That(result, Is.EqualTo(userResponse));
    }

    [Test]
    public async Task DeleteAsync_ShouldDeleteUser_WhenUserExists()
    {
        // Arrange
        var userRepositoryMock = new Mock<IUserRepository>();
        userRepositoryMock.Setup(repo => repo.Exists(1)).ReturnsAsync(true);

        var mapperMock = new Mock<IMapper>();

        var userService = new UserService(userRepositoryMock.Object, mapperMock.Object);

        // Act
        await userService.DeleteAsync(1);

        // Assert
        userRepositoryMock.Verify(repo => repo.DeleteAsync(1), Times.Once);
    }

    [Test]
    public async Task DeleteAsync_ShouldThrowException_WhenUserDoesNotExist()
    {
        // Arrange
        var userRepositoryMock = new Mock<IUserRepository>();
        userRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((User)null);

        var mapperMock = new Mock<IMapper>();

        var userService = new UserService(userRepositoryMock.Object, mapperMock.Object);

        // Act & Assert
        Assert.ThrowsAsync<KeyNotFoundException>(() => userService.DeleteAsync(1));
    }
    [Test]
    public async Task UpdateAsync_ShouldReturnUser_WhenUserExists()
    {
        // Arrange
        var userRequest = new UserRequestTo { id = 1, firstname = "Updated Label" };
        var user = new User { id = 1, firstname = "Updated Label" };
        var userResponse = new UserResponseTo { id = 1, firstname = "Updated Label" };
        
        var userRepositoryMock = new Mock<IUserRepository>();
        userRepositoryMock.Setup(repo => repo.Exists(1)).ReturnsAsync(true);
        userRepositoryMock.Setup(repo => repo.UpdateAsync(user)).ReturnsAsync(user);
        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<UserResponseTo>(user)).Returns(userResponse);
        mapperMock.Setup(mapper => mapper.Map<User>(userRequest)).Returns(user);
        var labelService = new UserService(userRepositoryMock.Object, mapperMock.Object);

        var result =await labelService.UpdateAsync(userRequest);
        Xunit.Assert.Equal(userResponse, result);
    }
    [Test]
    public async Task UpdateAsync_ShouldThrowException_WhenUserDoesNotExist()
    {
        // Arrange
        var userRequest = new UserRequestTo { id = 1, firstname = "Updated Label" };

        var userRepositoryMock = new Mock<IUserRepository>();
        userRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((User)null);

        var mapperMock = new Mock<IMapper>();

        var userService = new UserService(userRepositoryMock.Object, mapperMock.Object);

        // Act & Assert
        Assert.ThrowsAsync<KeyNotFoundException>(() => userService.UpdateAsync(userRequest));
    }
    
    [Test]
    public async Task GetById_ShouldReturnUser_WhenUserExists()
    {
        var user = new User() { id = 1, firstname = "Label 1" };
        var userResponse = new UserResponseTo() { id = 1, firstname = "Label 1" };
        // Arrange
        var userRepositoryMock = new Mock<IUserRepository>();
        userRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync(user);

        var mapperMock = new Mock<IMapper>();
        
        var userService = new UserService(userRepositoryMock.Object, mapperMock.Object);
        mapperMock.Setup(mapper => mapper.Map<UserResponseTo>(user)).Returns(userResponse);
        // Act
        var result = await userService.GetByIdAsync(1);
        Assert.NotNull(result);
        // Assert
        Assert.That(result, Is.EqualTo(userResponse));
    }

    [Test]
    public async Task GetById_ShouldThrowException_WhenUserDoesNotExist()
    {
        // Arrange
        var userRepositoryMock = new Mock<IUserRepository>();
        userRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((User)null);

        var mapperMock = new Mock<IMapper>();

        var userService = new UserService(userRepositoryMock.Object, mapperMock.Object);

        // Act & Assert
        Assert.ThrowsAsync<KeyNotFoundException>(() => userService.GetByIdAsync(1));
    }
}