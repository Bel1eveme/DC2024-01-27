using Microsoft.AspNetCore.Mvc;
using Moq;
using Publisher.Controllers;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Services.interfaces;
using Xunit;
using Assert = Xunit.Assert;

namespace Tests.ControllerTests;

public class NewsControllerTests
{
    [Fact]
    public Task GetAll_ShouldReturnAllEntities()
    {
        var newsServiceMock = new Mock<INewsService>();
        var responses = new List<NewsResponseTo>
        {
            new NewsResponseTo() { id = 1, title = "Title 1 "},
            new NewsResponseTo() { id = 2, title = "Title 2"}
        };
        newsServiceMock.Setup(service => service.GetAllAsync()).ReturnsAsync(responses);

        var controller = new NewsController(newsServiceMock.Object);
        var result = controller.GetAllNews();
        
        // Act
        var okResult = Assert.IsType<OkObjectResult>(result.Result.Result);
        var model = Assert.IsAssignableFrom<IEnumerable<NewsResponseTo>>(okResult.Value);
        Assert.Equal(responses, model);
        return Task.CompletedTask;
    }

    [Fact]
    public Task GetAll_ShouldReturn_NoEntites()
    {
        var empty = new List<NewsResponseTo>();
        var newsServiceMock = new Mock<INewsService>();
        newsServiceMock.Setup(service => service.GetAllAsync()).ReturnsAsync(new List<NewsResponseTo>());

        var controller = new NewsController(newsServiceMock.Object);
        var result = controller.GetAllNews();

        var okResult = Assert.IsType<OkObjectResult>(result.Result.Result);
        var model = Assert.IsAssignableFrom<IEnumerable<NewsResponseTo>>(okResult.Value);
        Assert.Equal(empty,model);
        return Task.CompletedTask;
    }

    [Fact]
    public Task GetById_ShouldReturn_News()
    {
        var news = new NewsResponseTo() { id = 1, title = "Name 1" };
        var newsServiceMock = new Mock<INewsService>();
        newsServiceMock.Setup(service => service.GetByIdAsync(1)).ReturnsAsync(news);

        var controller = new NewsController(newsServiceMock.Object);
        var result = controller.GetNewsById(1);
        
        var okResult = Assert.IsType<OkObjectResult>(result.Result.Result);
        var model = Assert.IsAssignableFrom<NewsResponseTo>(okResult.Value);
        Assert.Equal(news,model);
        return Task.CompletedTask;
    }
    
    [Fact]
    public async Task GetById_ShouldReturnNULL_WhenNewsDoesNotExist()
    {
        var newsServiceMock = new Mock<INewsService>();
        newsServiceMock.Setup(service => service.GetByIdAsync(1)).ReturnsAsync((NewsResponseTo)null);
        
        var controller = new NewsController(newsServiceMock.Object);
        var result = controller.GetNewsById(1);
        Assert.Null(result.Result.Value);
    }
    
    [Fact]
    public async Task CreateNews_WithValidModel_ReturnsCreatedAtAction()
    {
        // Arrange
        var newsRequest = new NewsRequestTo { id = 1, title = "Title 1", content = "abcd"};
        var newsAdded = new NewsResponseTo { id = 1, title = "Title 1", content = "abcd" };

        var newsServiceMock = new Mock<INewsService>();
        newsServiceMock.Setup(service => service.AddAsync(newsRequest)).ReturnsAsync(newsAdded);

        var controller = new NewsController(newsServiceMock.Object);

        // Act
        ActionResult<NewsResponseTo> result = await controller.CreateNews(newsRequest);

        // Assert
        Assert.NotNull(result);

        var actionResult = result.Result as CreatedAtActionResult;
        Assert.Equal(nameof(NewsController.GetNewsById), actionResult.ActionName);
        Assert.Equal(newsAdded, actionResult.Value);
    }

    [Fact]
    public async Task CreateNews_WithInvalidModel_ReturnsBadRequest()
    {
        // Arrange
        var newsRequest = new NewsRequestTo { id = 1, title = "Title 1", content = "abc"};

        var controller = new NewsController(Mock.Of<INewsService>());
            
        controller.ModelState.AddModelError("login", "Error message");

        // Act
        var result = await controller.CreateNews(newsRequest) as ActionResult<NewsResponseTo>;
        Assert.NotNull(result);
        
        var actionresult = result.Result as BadRequestObjectResult;
        // Assert
        Assert.Equal(400, actionresult.StatusCode);
    }
    
    [Fact]
    public async Task UpdateNews_WithValidModel_ReturnsOk()
    {
        // Arrange
        var newsRequest = new NewsRequestTo { id = 1, title = "Title 1", content = "abcd" };
        var newsUpdated = new NewsResponseTo { id = 1, title = "Title 1", content = "abcd" };

        var newsServiceMock = new Mock<INewsService>();
        newsServiceMock.Setup(service => service.UpdateAsync(It.IsAny<NewsRequestTo>())).ReturnsAsync(newsUpdated);

        var controller = new NewsController(newsServiceMock.Object);

        // Act
        var result = await controller.UpdateNews(newsRequest) as ActionResult<NewsResponseTo>;
        Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as IActionResult;
        Assert.IsAssignableFrom<OkObjectResult>(actionResult);
    }

    [Fact]
    public async Task UpdateNews_WithInvalidModel_ReturnsBadRequest()
    {
        // Arrange
        var newsRequest = new NewsRequestTo { id = 1, title = "Title 1", content = "abc" };

        var controller = new NewsController(Mock.Of<INewsService>());
        controller.ModelState.AddModelError("Login", "Error message");

        // Act
        var result = await controller.UpdateNews(newsRequest) as ActionResult<NewsResponseTo>;
        Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as BadRequestObjectResult;
        Assert.Equal(400, actionResult.StatusCode);
    }

    [Fact]
    public async Task UpdateNews_ThrowsException_ReturnsNotFound()
    {
        // Arrange
        var newsRequest = new NewsRequestTo { /* заполните поля, чтобы запрос был валидным */ };

        var newsServiceMock = new Mock<INewsService>();
        newsServiceMock.Setup(service => service.UpdateAsync(It.IsAny<NewsRequestTo>())).ThrowsAsync(new Exception());

        var controller = new NewsController(newsServiceMock.Object);

        // Act
        var result = await controller.UpdateNews(newsRequest) as ActionResult<NewsResponseTo>;
        Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as NotFoundObjectResult;
        Assert.Equal(404, actionResult.StatusCode);
    }
    
    [Fact]
    public async Task DeleteNews_WithValidId_ReturnsNoContent()
    {
        // Arrange
        int newsId = 1;

        var newsServiceMock = new Mock<INewsService>();
        newsServiceMock.Setup(service => service.DeleteAsync(newsId)).Verifiable();

        var controller = new NewsController(newsServiceMock.Object);

        // Act
        var result = await controller.DeleteUser(newsId) as ActionResult<NewsResponseTo>;
        Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as IActionResult;
        Assert.IsAssignableFrom<NoContentResult>(actionResult);

        newsServiceMock.Verify(); // Проверяем, что метод DeleteAsync был вызван
    }

    [Fact]
    public async Task DeleteNews_WithInvalidId_ReturnsNotFound()
    {
        // Arrange
        int newsId = -1; // Некорректный ID

        var newsServiceMock = new Mock<INewsService>();
        newsServiceMock.Setup(service => service.DeleteAsync(newsId)).ThrowsAsync(new Exception());

        var controller = new NewsController(newsServiceMock.Object);

        // Act
        var result = await controller.DeleteUser(newsId) as ActionResult<NewsResponseTo>;
        Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as NotFoundObjectResult;
        Assert.Equal(404, actionResult.StatusCode);
    }
}