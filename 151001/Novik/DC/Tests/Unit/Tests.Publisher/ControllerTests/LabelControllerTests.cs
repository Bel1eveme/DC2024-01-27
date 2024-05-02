using Microsoft.AspNetCore.Mvc;
using Moq;
using Publisher.Controllers;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Services.interfaces;
using Xunit;
using Assert = NUnit.Framework.Assert;
namespace Tests.ControllerTests;

public class LabelControllerTests
{
    [Fact]
    public Task GetAll_ShouldReturnAllEntities()
    {
        var labelServiceMock = new Mock<ILabelService>();
        var responses = new List<LabelResponseTo>
        {
            new LabelResponseTo() { id = 1, name = "Name 1"},
            new LabelResponseTo() { id = 2, name = "name 2" }
        };
        labelServiceMock.Setup(service => service.GetAllAsync()).ReturnsAsync(responses);

        var controller = new LabelsController(labelServiceMock.Object);
        var result = controller.GetAllLabels();
        
        // Act
        var okResult = Xunit.Assert.IsType<OkObjectResult>(result.Result.Result);
        var model = Xunit.Assert.IsAssignableFrom<IEnumerable<LabelResponseTo>>(okResult.Value);
        Xunit.Assert.Equal(responses, model);
        return Task.CompletedTask;
    }

    [Fact]
    public Task GetAll_ShouldReturn_NoEntites()
    {
        var empty = new List<LabelResponseTo>();
        var labelServiceMock = new Mock<ILabelService>();
        labelServiceMock.Setup(service => service.GetAllAsync()).ReturnsAsync(new List<LabelResponseTo>());

        var controller = new LabelsController(labelServiceMock.Object);
        var result = controller.GetAllLabels();

        var okResult = Xunit.Assert.IsType<OkObjectResult>(result.Result.Result);
        var model = Xunit.Assert.IsAssignableFrom<IEnumerable<LabelResponseTo>>(okResult.Value);
        Xunit.Assert.Equal(empty,model);
        return Task.CompletedTask;
    }

    [Fact]
    public Task GetById_ShouldReturn_Label()
    {
        var label = new LabelResponseTo() { id = 1, name = "Name 1" };
        var labelServiceMock = new Mock<ILabelService>();
        labelServiceMock.Setup(service => service.GetByIdAsync(1)).ReturnsAsync(label);

        var controller = new LabelsController(labelServiceMock.Object);
        var result = controller.GetById(1);
        
        var okResult = Xunit.Assert.IsType<OkObjectResult>(result.Result.Result);
        var model = Xunit.Assert.IsAssignableFrom<LabelResponseTo>(okResult.Value);
        Xunit.Assert.Equal(label,model);
        return Task.CompletedTask;
    }
    
    [Fact]
    public async Task GetById_ShouldReturnNULL_WhenLabelDoesNotExist()
    {
        var labelServiceMock = new Mock<ILabelService>();
        labelServiceMock.Setup(service => service.GetByIdAsync(1)).ReturnsAsync((LabelResponseTo)null);
        
        var controller = new LabelsController(labelServiceMock.Object);
        var result = controller.GetById(1);
        Xunit.Assert.Null(result.Result.Value);
    }
    
    [Fact]
    public async Task CreateLabel_WithValidModel_ReturnsCreatedAtAction()
    {
        // Arrange
        var labelRequest = new LabelRequestTo { id = 1, name = "abcd"};
        var labelAdded = new LabelResponseTo { id = 1, name = "abcd" };

        var labelServiceMock = new Mock<ILabelService>();
        labelServiceMock.Setup(service => service.AddAsync(labelRequest)).ReturnsAsync(labelAdded);

        var controller = new LabelsController(labelServiceMock.Object);

        // Act
        ActionResult<LabelResponseTo> result = await controller.CreateLabel(labelRequest);

        // Assert
        Xunit.Assert.NotNull(result);

        var actionResult = result.Result as CreatedAtActionResult;
        Xunit.Assert.Equal(nameof(LabelsController.GetById), actionResult.ActionName);
        Xunit.Assert.Equal(labelAdded, actionResult.Value);
    }

    [Fact]
    public async Task CreateLabel_WithInvalidModel_ReturnsBadRequest()
    {
        // Arrange
        var labelRequest = new LabelRequestTo { id = 1, name = "a"};

        var controller = new LabelsController(Mock.Of<ILabelService>());
            
        controller.ModelState.AddModelError("login", "Error message");

        // Act
        var result = await controller.CreateLabel(labelRequest) as ActionResult<LabelResponseTo>;
        Xunit.Assert.NotNull(result);
        
        var actionresult = result.Result as BadRequestObjectResult;
        // Assert
        Xunit.Assert.Equal(400, actionresult.StatusCode);
    }
    
    [Fact]
    public async Task UpdateLabel_WithValidModel_ReturnsOk()
    {
        // Arrange
        var labelRequest = new LabelRequestTo { id = 1, name = "abcd" };
        var labelUpdated = new LabelResponseTo { id = 1, name = "abcd" };

        var labelServiceMock = new Mock<ILabelService>();
        labelServiceMock.Setup(service => service.UpdateAsync(It.IsAny<LabelRequestTo>())).ReturnsAsync(labelUpdated);

        var controller = new LabelsController(labelServiceMock.Object);

        // Act
        var result = await controller.UpdateLabel(labelRequest) as ActionResult<LabelResponseTo>;
        Xunit.Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as IActionResult;
        Xunit.Assert.IsAssignableFrom<OkObjectResult>(actionResult);
    }

    [Fact]
    public async Task UpdateLabel_WithInvalidModel_ReturnsBadRequest()
    {
        // Arrange
        var userRequest = new LabelRequestTo { id = 1, name = "a" };

        var controller = new LabelsController(Mock.Of<ILabelService>());
        controller.ModelState.AddModelError("Login", "Error message");

        // Act
        var result = await controller.UpdateLabel(userRequest) as ActionResult<LabelResponseTo>;
        Xunit.Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as BadRequestObjectResult;
        Xunit.Assert.Equal(400, actionResult.StatusCode);
    }

    [Fact]
    public async Task UpdateLabel_ThrowsException_ReturnsNotFound()
    {
        // Arrange
        var labelRequest = new LabelRequestTo { id = 1, name = "abcd" };

        var labelServiceMock = new Mock<ILabelService>();
        labelServiceMock.Setup(service => service.UpdateAsync(It.IsAny<LabelRequestTo>())).ThrowsAsync(new Exception());

        var controller = new LabelsController(labelServiceMock.Object);

        // Act
        var result = await controller.UpdateLabel(labelRequest) as ActionResult<LabelResponseTo>;
        Xunit.Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as NotFoundObjectResult;
        Xunit.Assert.Equal(404, actionResult.StatusCode);
    }
    
    [Fact]
    public async Task DeleteLabel_WithValidId_ReturnsNoContent()
    {
        // Arrange
        int labelId = 1;

        var labelServiceMock = new Mock<ILabelService>();
        labelServiceMock.Setup(service => service.DeleteAsync(labelId)).Verifiable();

        var controller = new LabelsController(labelServiceMock.Object);

        // Act
        var result = await controller.DeleteLabel(labelId) as ActionResult<LabelResponseTo>;
        Xunit.Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as IActionResult;
        Xunit.Assert.IsAssignableFrom<NoContentResult>(actionResult);

        labelServiceMock.Verify(); // Проверяем, что метод DeleteAsync был вызван
    }

    [Fact]
    public async Task DeleteLabel_WithInvalidId_ReturnsNotFound()
    {
        // Arrange
        int labelId = -1; // Некорректный ID

        var labelServiceMock = new Mock<ILabelService>();
        labelServiceMock.Setup(service => service.DeleteAsync(labelId)).ThrowsAsync(new Exception());

        var controller = new LabelsController(labelServiceMock.Object);

        // Act
        var result = await controller.DeleteLabel(labelId) as ActionResult<LabelResponseTo>;
        Xunit.Assert.NotNull(result);
        // Assert
        var actionResult = result.Result as NotFoundObjectResult;
        Xunit.Assert.Equal(404, actionResult.StatusCode);
    }
}