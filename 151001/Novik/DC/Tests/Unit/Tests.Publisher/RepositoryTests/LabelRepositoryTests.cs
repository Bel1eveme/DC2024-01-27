using System.Collections;
using Microsoft.AspNetCore.Mvc;
using Moq;
using Publisher.Models.Entity;
using Publisher.Repositories.interfaces;


namespace Tests.RepositoryTests;

public class LabelRepositoryTests
{

    [Test]
    public async Task AddAsync_ShouldAddEntity()
    {
        // Arrange
        var entity = new Label { id = 1, name = "Label 1" };
        var labelRepoMock = new Mock<ILabelRepository>();
        labelRepoMock.Setup(repo => repo.AddAsync(entity)).ReturnsAsync(entity);
        // Act
        var result = await labelRepoMock.Object.AddAsync(entity);

        // Assert
        Assert.NotNull(result);
        Assert.That(result, Is.EqualTo(entity));
    }
    [Test]
    public async Task GetByIdAsync_ShouldReturnEntity()
    {
        // Arrange
        var entities = new List<Label> { new Label { id = 1, name = "Label 1" } };
        var labelRepoMock = new Mock<ILabelRepository>();
        labelRepoMock.Setup(repo => repo.AddAsync(entities[0])).ReturnsAsync(entities[0]);
        // Act
        var result = await labelRepoMock.Object.AddAsync(entities[0]);

        // Assert
        Assert.NotNull(result);
        Assert.That(result, Is.EqualTo(entities[0]));
    }
    
    [Test]
    public async Task GetByIdAsync_EntityNotExists()
    {
        // Arrange
        var labelRepoMock = new Mock<ILabelRepository>();
        labelRepoMock.Setup(repo => repo.GetByIdAsync(1)).ReturnsAsync((Label)null);
        // Act
        var result = await labelRepoMock.Object.GetByIdAsync(1);

        // Assert
        Assert.That(result, Is.EqualTo(null));
    }

    [Test]
    public async Task GetAllAsync_ShouldReturnAllEntities()
    {
        // Arrange
        var entities = new List<Label>
        {
            new Label { id = 1, name = "Label 1" },
            new Label { id = 2, name = "Label 2" }
        };
        var labelRepoMock = new Mock<ILabelRepository>();
        labelRepoMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(entities);
        
        foreach (var entity in entities)
        {
            await labelRepoMock.Object.AddAsync(entity);
        }

        // Act
        var result = await labelRepoMock.Object.GetAllAsync();

        // Assert
        Assert.NotNull(result);
        Assert.That(result.Count(), Is.EqualTo(2));
    }
    
    [Test]
    public async Task GetAllAsync_NoEntities()
    {
        // Arrange
        var labelRepoMock = new Mock<ILabelRepository>();
        var inserted = new Dictionary<long, Label>();
        labelRepoMock.Setup(repo => repo.GetAllAsync()).ReturnsAsync(inserted.Values);
        // Act
        var result = await labelRepoMock.Object.GetAllAsync();
        var expected = new Dictionary<long, Label>();
        // Assert
        Assert.That(result, Is.EqualTo(expected));
    }
    
    [Test]
    public async Task UpdateAsync_ShouldUpdateEntity()
    {
        // Arrange
        var entity = new Label { id = 1, name = "Updated Label" };
        var labelRepoMock = new Mock<ILabelRepository>();
        labelRepoMock.Setup(repo => repo.UpdateAsync(entity)).ReturnsAsync(entity);
        
        // Act
        var result = await labelRepoMock.Object.UpdateAsync(entity);

        // Assert
        Assert.NotNull(result);
        Assert.That(result.name, Is.EqualTo("Updated Label"));
    }

    [Test]
    public async Task DeleteAsync_ShouldRemoveEntity()
    {
        var labelRepoMock = new Mock<ILabelRepository>();
        labelRepoMock.Setup(repo => repo.DeleteAsync(1)).Returns(Task.CompletedTask);
        
        Task task =  labelRepoMock.Object.DeleteAsync(1);

        // Assert
        // Verify that DeleteAsync method was called
        labelRepoMock.Verify(repo => repo.DeleteAsync(1),Times.Once);
    }
    
    [Test]
    public async Task DeleteAsync_NoEntities()
    {
        var labelRepoMock = new Mock<ILabelRepository>();
        labelRepoMock.Setup(repo => repo.DeleteAsync(1)).Returns(Task.CompletedTask);
        
        Task task =  labelRepoMock.Object.DeleteAsync(1);

        // Assert
        // Verify that DeleteAsync method was called
        labelRepoMock.Verify(repo => repo.DeleteAsync(1),Times.Once);
    }

    [Test]
    public async Task ExistsAsync_Entity_ExpectedTrue()
    {
        var labelRepoMock = new Mock<ILabelRepository>();
        labelRepoMock.Setup(repo => repo.Exists(1)).ReturnsAsync(true);

        var result = await labelRepoMock.Object.Exists(1);
        var expected = true;
        Assert.That(result,Is.EqualTo(expected));
    }
    
    [Test]
    public async Task ExistsAsync_NoEntity_ExpectedFalse()
    {
        var labelRepoMock = new Mock<ILabelRepository>();
        labelRepoMock.Setup(repo => repo.Exists(1)).ReturnsAsync(false);

        var result = await labelRepoMock.Object.Exists(1);
        var expected = false;
        Assert.That(result,Is.EqualTo(expected));
    }
    
}