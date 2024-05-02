using Microsoft.EntityFrameworkCore;
using LR2.Dto.Request.CreateTo;
using LR2.Dto.Request.UpdateTo;
using LR2.Dto.Response;
using LR2.Exceptions;
using LR2.Services.Interfaces;
using LR2.Storage;
using LabelMapper = LR2.Mappers.LabelMapper;

namespace LR2.Services.Implementations;

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