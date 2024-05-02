using LR2.Dto.Request.CreateTo;
using LR2.Dto.Request.UpdateTo;
using LR2.Dto.Response;

namespace LR2.Services.Interfaces;

public interface ILabelService
{
    Task<LabelResponseTo> GetLabelById(long id);
    Task<IEnumerable<LabelResponseTo>> GetLabels();
    Task<LabelResponseTo> CreateLabel(CreateLabelRequestTo createLabelRequestTo);
    Task DeleteLabel(long id);
    Task<LabelResponseTo> UpdateLabel(UpdateLabelRequestTo modifiedLabel);
}