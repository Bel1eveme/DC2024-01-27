using Microsoft.EntityFrameworkCore;
using LR1.Dto.Request.CreateTo;
using LR1.Dto.Request.UpdateTo;
using LR1.Dto.Response;
using LR1.Exceptions;
using LR1.Services.Interfaces;
using LR1.Storage;
using LabelMapper = LR1.Mappers.LabelMapper;

namespace LR1.Services.Implementations;

public sealed class LabelService(AppDbContext context) : ILabelService
{
    public async Task<LabelResponseTo> GetLabelById(long id)
    {
        var Label = await context.Labels.FindAsync(id);
        if (Label == null) throw new EntityNotFoundException($"Label with id = {id} doesn't exist.");

        return LabelMapper.Map(Label);
    }

    public async Task<IEnumerable<LabelResponseTo>> GetLabels()
    {
        return LabelMapper.Map(await context.Labels.ToListAsync());
    }

    public async Task<LabelResponseTo> CreateLabel(CreateLabelRequestTo createLabelRequestTo)
    {
        var Label = LabelMapper.Map(createLabelRequestTo);
        await context.Labels.AddAsync(Label);
        await context.SaveChangesAsync();
        return LabelMapper.Map(Label);
    }

    public async Task DeleteLabel(long id)
    {
        var Label = await context.Labels.FindAsync(id);
        if (Label == null) throw new EntityNotFoundException($"Label with id = {id} doesn't exist.");

        context.Labels.Remove(Label);
        await context.SaveChangesAsync();
    }

    public async Task<LabelResponseTo> UpdateLabel(UpdateLabelRequestTo modifiedLabel)
    {
        var Label = await context.Labels.FindAsync(modifiedLabel.Id);
        if (Label == null) throw new EntityNotFoundException($"Label with id = {modifiedLabel.Id} doesn't exist.");

        context.Entry(Label).State = EntityState.Modified;

        Label.Id = modifiedLabel.Id;
        Label.Name = modifiedLabel.Name;

        await context.SaveChangesAsync();
        return LabelMapper.Map(Label);
    }
}