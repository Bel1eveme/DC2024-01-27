using Discussion.Controllers;
using Discussion.Models.DTOs.Requests;
using Discussion.Models.DTOs.Responses;
using Discussion.Services.interfaces;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Moq;

namespace Tests.Discussion.ControllerTests;

public class PostsControllerTests
{

    [Fact]
    public Task GetAll_ShouldReturnAllEntities()
    {
        var postServiceMock = new Mock<IPostService>();
        var responses = new List<PostResponseTo>
        {
            new PostResponseTo() { id = 1, content = "User 1" },
            new PostResponseTo() { id = 2, content = "User 2" }
        };
        postServiceMock.Setup(service => service.GetAllAsync()).ReturnsAsync(responses);

        var controller = new PostsController(postServiceMock.Object);
        var result = controller.GetAllPosts();
        
        // Act
        var okResult = Assert.IsType<OkObjectResult>(result.Result.Result);
        var model = Assert.IsAssignableFrom<IEnumerable<PostResponseTo>>(okResult.Value);
        Assert.Equal(responses, model);
        return Task.CompletedTask;
    }

    [Fact]
    public Task GetAll_ShouldReturn_NoEntites()
    {
        var empty = new List<PostResponseTo>();
        var postServiceMock = new Mock<IPostService>();
        postServiceMock.Setup(service => service.GetAllAsync()).ReturnsAsync(new List<PostResponseTo>());

        var controller = new PostsController(postServiceMock.Object);
        var result = controller.GetAllPosts();

        var okResult = Assert.IsType<OkObjectResult>(result.Result.Result);
        var model = Assert.IsAssignableFrom<IEnumerable<PostResponseTo>>(okResult.Value);
        Assert.Equal(empty,model);
        return Task.CompletedTask;
    }

    [Fact]
    public async Task GetById_ShouldReturn_Post()
    {
        var post = new PostResponseTo() { id = 1, content = "Name 1" };
        var postServiceMock = new Mock<IPostService>();
        postServiceMock.Setup(service => service.GetByIdAsync(1)).ReturnsAsync(post);

        var controller = new PostsController(postServiceMock.Object);
        var result = await controller.GetPostById(1);
        
        var okResult = Assert.IsType<OkObjectResult>(result.Result);
        var model = Assert.IsAssignableFrom<PostResponseTo>(okResult.Value);
        Assert.Equal(post,model);
    }
    
    [Fact]
    public async Task GetById_ShouldReturnNULL_WhenPostDoesNotExist()
    {
        var postServiceMock = new Mock<IPostService>();
        postServiceMock.Setup(service => service.GetByIdAsync(1)).ReturnsAsync((PostResponseTo)null);
        
        var controller = new PostsController(postServiceMock.Object);
        var result = await controller.GetPostById(1);
        Assert.Null(result.Value);
    }
    
    [Fact]
    public async Task CreatePost_WithValidModel_ReturnsCreatedAtAction()
    {
        // Arrange
        var postRequest = new PostRequestTo { id = 1, content = "abcd"};
        var postAdded = new PostResponseTo { id = 1, content = "abcd"};

        var postServiceMock = new Mock<IPostService>();
        postServiceMock.Setup(service => service.AddAsync(postRequest, "ru")).ReturnsAsync(postAdded);

        var controller = new PostsController(postServiceMock.Object);
        var httpContext = new DefaultHttpContext();
        httpContext.Request.Headers["Accept-Language"] = "ru"; // Set the Accept-Language header

        controller.ControllerContext = new ControllerContext()
        {
            HttpContext = httpContext,
        };
        // Act
        ActionResult<PostResponseTo> result = await controller.CreatePost(postRequest);

        // Assert
        Assert.NotNull(result);

        var actionResult = result.Result as CreatedAtActionResult;
        Assert.Equal(nameof(PostsController.GetPostById), actionResult.ActionName);
        Assert.Equal(postAdded, actionResult.Value);
    }

    [Fact]
    public async Task CreatePost_WithInvalidModel_ReturnsBadRequest()
    {
        // Arrange
        var postRequest = new PostRequestTo { id = 1, content = "a"};

        var controller = new PostsController(Mock.Of<IPostService>());
            
        controller.ModelState.AddModelError("login", "Error message");

        // Act
        var result = await controller.CreatePost(postRequest) as ActionResult<PostResponseTo>;
        Assert.NotNull(result);
        
        var actionresult = result.Result as BadRequestObjectResult;
        // Assert
        Assert.Equal(400, actionresult.StatusCode);
    }
    
    [Fact]
    public async Task UpdatePost_WithValidModel_ReturnsOk()
    {
        // Arrange
        var postRequest = new PostRequestTo { id = 1, content = "abcd"};
        var postUpdated = new PostResponseTo { id = 1, content = "abcd"};

        var postServiceMock = new Mock<IPostService>();
        postServiceMock.Setup(service => service.UpdateAsync(It.IsAny<PostRequestTo>(),"ru")).ReturnsAsync(postUpdated);

        var controller = new PostsController(postServiceMock.Object);
        var httpContext = new DefaultHttpContext();
        httpContext.Request.Headers["Accept-Language"] = "ru"; // Set the Accept-Language header

        controller.ControllerContext = new ControllerContext()
        {
            HttpContext = httpContext,
        };
        

        // Act
        var result = await controller.UpdatePost(postRequest) as ActionResult<PostResponseTo>;
        Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as IActionResult;
        Assert.IsAssignableFrom<OkObjectResult>(actionResult);
    }

    [Fact]
    public async Task UpdatePost_WithInvalidModel_ReturnsBadRequest()
    {
        // Arrange
        var postRequest = new PostRequestTo { id = 1, content = "a"};

        var controller = new PostsController(Mock.Of<IPostService>());
        controller.ModelState.AddModelError("Login", "Error message");

        // Act
        var result = await controller.UpdatePost(postRequest) as ActionResult<PostResponseTo>;
        Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as BadRequestObjectResult;
        Assert.Equal(400, actionResult.StatusCode);
    }

    [Fact]
    public async Task UpdatePost_ThrowsException_ReturnsNotFound()
    {
        // Arrange
        var postRequest = new PostRequestTo { /* заполните поля, чтобы запрос был валидным */ };

        var postServiceMock = new Mock<IPostService>();
        postServiceMock.Setup(service => service.UpdateAsync(It.IsAny<PostRequestTo>(),"ru")).ThrowsAsync(new Exception());

        var controller = new PostsController(postServiceMock.Object);

        // Act
        var result = await controller.UpdatePost(postRequest) as ActionResult<PostResponseTo>;
        Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as NotFoundObjectResult;
        Assert.Equal(404, actionResult.StatusCode);
    }
    
    [Fact]
    public async Task DeletePost_WithValidId_ReturnsNoContent()
    {
        // Arrange
        int postId = 1;

        var postServiceMock = new Mock<IPostService>();
        postServiceMock.Setup(service => service.DeleteAsync(postId)).Verifiable();

        var controller = new PostsController(postServiceMock.Object);

        // Act
        var result = await controller.DeletePost(postId) as ActionResult<PostResponseTo>;
        Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as IActionResult;
        Assert.IsAssignableFrom<NoContentResult>(actionResult);

        postServiceMock.Verify(); // Проверяем, что метод DeleteAsync был вызван
    }

    [Fact]
    public async Task DeletePost_WithInvalidId_ReturnsNotFound()
    {
        // Arrange
        int postId = -1; // Некорректный ID

        var postServiceMock = new Mock<IPostService>();
        postServiceMock.Setup(service => service.DeleteAsync(postId)).ThrowsAsync(new Exception());

        var controller = new PostsController(postServiceMock.Object);

        // Act
        var result = await controller.DeletePost(postId) as ActionResult<PostResponseTo>;
        Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as NotFoundObjectResult;
        Assert.Equal(404, actionResult.StatusCode);
    }
}