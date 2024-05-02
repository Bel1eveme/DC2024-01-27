using AutoMapper;
using Moq;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Models.Entity;
using Publisher.Repositories.interfaces;
using Publisher.Services;


namespace Tests.ServiceTests;

public class LabelServiceTests
{
    [Test]
    public async Task GetAllAsync_ShouldReturnAllLabels()
    {
        // Arrange
        var labelEntities = new List<Label>
        {
            new Label { id = 1, name = "Label 1" },
            new Label { id = 2, name = "Label 2" }
        };
        var labelResponseTo = new List<LabelResponseTo>
        {
            new LabelResponseTo { id = 1, name = "Label 1" },
            new LabelResponseTo { id = 2, name = "Label 2" }
        };
        
        var labelRepositoryMock = new Mock<ILabelRepository>();
        labelRepositoryMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(labelEntities);

        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<IEnumerable<LabelResponseTo>>(labelEntities)).Returns(labelResponseTo);

        var labelService = new LabelService(labelRepositoryMock.Object, mapperMock.Object);

        // Act
        var result = await labelService.GetAllAsync();

        // Assert
        Assert.NotNull(result);
        Assert.That(result.Count(), Is.EqualTo(2));
    }
    
    [Test]
    public async Task GetAllAsync_WhenLabelsDontExist()
    {
        // Arrange
        var labelEntities = new List<Label>();

        var labelResponseTo = new List<LabelResponseTo>();
        
        var labelRepositoryMock = new Mock<ILabelRepository>();
        labelRepositoryMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(labelEntities);

        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<IEnumerable<LabelResponseTo>>(labelEntities)).Returns(labelResponseTo);

        var labelService = new LabelService(labelRepositoryMock.Object, mapperMock.Object);

        // Act
        var result = await labelService.GetAllAsync();

        // Assert
        Assert.That(result.Count(), Is.EqualTo(0));
    }
    
    [Test]
    public async Task AddAsync_ExistingEntity()
    {
        // Arrange
        var label = new Label { id = 1, name = "Label 1" };

        var labelRequest = new LabelRequestTo() { id = 1, name = "Label 1" };

        var labelResponse = new LabelResponseTo { id = 1, name = "Label 1" };
        
        var labelRepositoryMock = new Mock<ILabelRepository>();
        labelRepositoryMock.Setup(repo => repo.AddAsync(label)).ReturnsAsync(label);

        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<LabelResponseTo>(label)).Returns(labelResponse);
        mapperMock.Setup(mapper => mapper.Map<Label>(labelRequest)).Returns(label);
        
        var labelService = new LabelService(labelRepositoryMock.Object, mapperMock.Object);

        // Act
        var result = await labelService.AddAsync(labelRequest);

        // Assert
        Assert.NotNull(result);
        Assert.That(result, Is.EqualTo(labelResponse));
    }

    [Test]
    public async Task DeleteAsync_ShouldDeleteLabel_WhenLabelExists()
    {
        // Arrange
        var labelRepositoryMock = new Mock<ILabelRepository>();
        labelRepositoryMock.Setup(repo => repo.Exists(1)).ReturnsAsync(true);

        var mapperMock = new Mock<IMapper>();

        var labelService = new LabelService(labelRepositoryMock.Object, mapperMock.Object);

        // Act
        await labelService.DeleteAsync(1);

        // Assert
        labelRepositoryMock.Verify(repo => repo.DeleteAsync(1), Times.Once);
    }

    [Test]
    public async Task DeleteAsync_ShouldThrowException_WhenLabelDoesNotExist()
    {
        // Arrange
        var labelRepositoryMock = new Mock<ILabelRepository>();
        labelRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((Label)null);

        var mapperMock = new Mock<IMapper>();

        var labelService = new LabelService(labelRepositoryMock.Object, mapperMock.Object);

        // Act & Assert
        Assert.ThrowsAsync<KeyNotFoundException>(() => labelService.DeleteAsync(1));
    }
    
    [Test]
    public async Task UpdateAsync_ShouldReturnLabel_WhenLabelExists()
    {
        // Arrange
        var labelRequest = new LabelRequestTo { id = 1, name = "Updated Label" };
        var label = new Label { id = 1, name = "Updated Label" };
        var labelResponse = new LabelResponseTo { id = 1, name = "Updated Label" };
        
        var labelRepositoryMock = new Mock<ILabelRepository>();
        labelRepositoryMock.Setup(repo => repo.Exists(1)).ReturnsAsync(true);
        labelRepositoryMock.Setup(repo => repo.UpdateAsync(label)).ReturnsAsync(label);
        var mapperMock = new Mock<IMapper>();
        mapperMock.Setup(mapper => mapper.Map<LabelResponseTo>(label)).Returns(labelResponse);
        mapperMock.Setup(mapper => mapper.Map<Label>(labelRequest)).Returns(label);
        var labelService = new LabelService(labelRepositoryMock.Object, mapperMock.Object);

        var result =await labelService.UpdateAsync(labelRequest);
        Xunit.Assert.Equal(labelResponse, result);
    }
    [Test]
    public async Task UpdateAsync_ShouldThrowException_WhenLabelDoesNotExist()
    {
        // Arrange
        var labelRequest = new LabelRequestTo { id = 1, name = "Updated Label" };

        var labelRepositoryMock = new Mock<ILabelRepository>();
        labelRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((Label)null);

        var mapperMock = new Mock<IMapper>();

        var labelService = new LabelService(labelRepositoryMock.Object, mapperMock.Object);

        // Act & Assert
        Assert.ThrowsAsync<KeyNotFoundException>(() => labelService.UpdateAsync(labelRequest));
    }
    
    [Test]
    public async Task GetById_ShouldReturnLabel_WhenLabelExists()
    {
        var label = new Label() { id = 1, name = "Label 1" };
        var labelResponse = new LabelResponseTo() { id = 1, name = "Label 1" };
        // Arrange
        var labelRepositoryMock = new Mock<ILabelRepository>();
        labelRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync(label);

        var mapperMock = new Mock<IMapper>();
        
        var labelService = new LabelService(labelRepositoryMock.Object, mapperMock.Object);
        mapperMock.Setup(mapper => mapper.Map<LabelResponseTo>(label)).Returns(labelResponse);
        // Act
        var result = await labelService.GetByIdAsync(1);

        // Assert
        Assert.That(result, Is.EqualTo(labelResponse));
    }

    [Test]
    public async Task GetById_ShouldThrowException_WhenLabelDoesNotExist()
    {
        // Arrange
        var labelRepositoryMock = new Mock<ILabelRepository>();
        labelRepositoryMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((Label)null);

        var mapperMock = new Mock<IMapper>();

        var labelService = new LabelService(labelRepositoryMock.Object, mapperMock.Object);

        // Act & Assert
        Assert.ThrowsAsync<KeyNotFoundException>(() => labelService.GetByIdAsync(1));
    }
}