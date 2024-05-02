
using Microsoft.AspNetCore.Mvc;
using Moq;
using Publisher.Controllers;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Services.interfaces;
using Xunit;
using Assert = Xunit.Assert;

namespace Tests.ControllerTests;

public class UserControllerTests
{
    [Fact]
    public Task GetAll_ShouldReturnAllEntities()
    {
        var userServiceMock = new Mock<IUserService>();
        var responses = new List<UserResponseTo>
        {
            new UserResponseTo() { id = 1, firstname = "User 1" },
            new UserResponseTo() { id = 2, firstname = "User 2" }
        };
        userServiceMock.Setup(service => service.GetAllAsync()).ReturnsAsync(responses);

        var controller = new UsersController(userServiceMock.Object);
        var result = controller.GetAllUsers();
        
        // Act
        var okResult = Assert.IsType<OkObjectResult>(result.Result.Result);
        var model = Assert.IsAssignableFrom<IEnumerable<UserResponseTo>>(okResult.Value);
        Assert.Equal(responses, model);
        return Task.CompletedTask;
    }

    [Fact]
    public Task GetAll_ShouldReturn_NoEntites()
    {
        var empty = new List<UserResponseTo>();
        var userServiceMock = new Mock<IUserService>();
        userServiceMock.Setup(service => service.GetAllAsync()).ReturnsAsync(new List<UserResponseTo>());

        var controller = new UsersController(userServiceMock.Object);
        var result = controller.GetAllUsers();

        var okResult = Assert.IsType<OkObjectResult>(result.Result.Result);
        var model = Assert.IsAssignableFrom<IEnumerable<UserResponseTo>>(okResult.Value);
        Assert.Equal(empty,model);
        return Task.CompletedTask;
    }

    [Fact]
    public Task GetById_ShouldReturn_User()
    {
        var user = new UserResponseTo() { id = 1, firstname = "Name 1" };
        var userServiceMock = new Mock<IUserService>();
        userServiceMock.Setup(service => service.GetByIdAsync(1)).ReturnsAsync(user);

        var controller = new UsersController(userServiceMock.Object);
        var result = controller.GetUserById(1);
        
        var okResult = Assert.IsType<OkObjectResult>(result.Result.Result);
        var model = Assert.IsAssignableFrom<UserResponseTo>(okResult.Value);
        Assert.Equal(user,model);
        return Task.CompletedTask;
    }
    
    [Fact]
    public async Task GetById_ShouldReturnNULL_WhenUserDoesNotExist()
    {
        var userServiceMock = new Mock<IUserService>();
        userServiceMock.Setup(service => service.GetByIdAsync(1)).ReturnsAsync((UserResponseTo)null);
        
        var controller = new UsersController(userServiceMock.Object);
        var result = controller.GetUserById(1);
        Assert.Null(result.Result.Value);
    }
    
    [Fact]
    public async Task CreateUser_WithValidModel_ReturnsCreatedAtAction()
    {
        // Arrange
        var userRequest = new UserRequestTo { id = 1, firstname = "abcd", lastname = "abcd", login = "abcd", password = "123456789"};
        var userAdded = new UserResponseTo { id = 1, firstname = "abcd", lastname = "abcd", login = "abcd", password = "123456789" };

        var userServiceMock = new Mock<IUserService>();
        userServiceMock.Setup(service => service.AddAsync(userRequest)).ReturnsAsync(userAdded);

        var controller = new UsersController(userServiceMock.Object);

        // Act
        ActionResult<UserResponseTo> result = await controller.CreateUser(userRequest);

        // Assert
        Assert.NotNull(result);

        var actionResult = result.Result as CreatedAtActionResult;
        Assert.Equal(nameof(UsersController.GetUserById), actionResult.ActionName);
        Assert.Equal(userAdded, actionResult.Value);
    }

    [Fact]
    public async Task CreateUser_WithInvalidModel_ReturnsBadRequest()
    {
        // Arrange
        var userRequest = new UserRequestTo { id = 1, login = "x", firstname = "abcd", lastname = "abcd", password = "123456789"};

        var controller = new UsersController(Mock.Of<IUserService>());
            
        controller.ModelState.AddModelError("login", "Error message");

        // Act
        var result = await controller.CreateUser(userRequest) as ActionResult<UserResponseTo>;
        Assert.NotNull(result);
        
        var actionresult = result.Result as BadRequestObjectResult;
        // Assert
        Assert.Equal(400, actionresult.StatusCode);
    }
    
    [Fact]
    public async Task UpdateUser_WithValidModel_ReturnsOk()
    {
        // Arrange
        var userRequest = new UserRequestTo { id = 1, firstname = "abcd", lastname = "abcd", login = "abcd", password = "123456789" };
        var userUpdated = new UserResponseTo { id = 1, firstname = "abcd", lastname = "abcd", login = "abcd", password = "123456789" };

        var userServiceMock = new Mock<IUserService>();
        userServiceMock.Setup(service => service.UpdateAsync(It.IsAny<UserRequestTo>())).ReturnsAsync(userUpdated);

        var controller = new UsersController(userServiceMock.Object);

        // Act
        var result = await controller.UpdateUser(userRequest) as ActionResult<UserResponseTo>;
        Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as IActionResult;
        Assert.IsAssignableFrom<OkObjectResult>(actionResult);
    }

    [Fact]
    public async Task UpdateUser_WithInvalidModel_ReturnsBadRequest()
    {
        // Arrange
        var userRequest = new UserRequestTo { id = 1, login = "x", firstname = "abcd", lastname = "abcd", password = "123456789" };

        var controller = new UsersController(Mock.Of<IUserService>());
        controller.ModelState.AddModelError("Login", "Error message");

        // Act
        var result = await controller.UpdateUser(userRequest) as ActionResult<UserResponseTo>;
        Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as BadRequestObjectResult;
        Assert.Equal(400, actionResult.StatusCode);
    }

    [Fact]
    public async Task UpdateUser_ThrowsException_ReturnsNotFound()
    {
        // Arrange
        var userRequest = new UserRequestTo { };

        var userServiceMock = new Mock<IUserService>();
        userServiceMock.Setup(service => service.UpdateAsync(It.IsAny<UserRequestTo>())).ThrowsAsync(new Exception());

        var controller = new UsersController(userServiceMock.Object);

        // Act
        var result = await controller.UpdateUser(userRequest) as ActionResult<UserResponseTo>;
        Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as NotFoundObjectResult;
        Assert.Equal(404, actionResult.StatusCode);
    }
    
    [Fact]
    public async Task DeleteUser_WithValidId_ReturnsNoContent()
    {
        // Arrange
        int userId = 1;

        var userServiceMock = new Mock<IUserService>();
        userServiceMock.Setup(service => service.DeleteAsync(userId)).Verifiable();

        var controller = new UsersController(userServiceMock.Object);

        // Act
        var result = await controller.DeleteUser(userId) as ActionResult<UserResponseTo>;
        Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as IActionResult;
        Assert.IsAssignableFrom<NoContentResult>(actionResult);

        userServiceMock.Verify(); // Проверяем, что метод DeleteAsync был вызван
    }

    [Fact]
    public async Task DeleteUser_WithInvalidId_ReturnsNotFound()
    {
        // Arrange
        int userId = -1; // Некорректный ID

        var userServiceMock = new Mock<IUserService>();
        userServiceMock.Setup(service => service.DeleteAsync(userId)).ThrowsAsync(new Exception());

        var controller = new UsersController(userServiceMock.Object);

        // Act
        var result = await controller.DeleteUser(userId) as ActionResult<UserResponseTo>;
        Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as NotFoundObjectResult;
        Assert.Equal(404, actionResult.StatusCode);
    }

   
}