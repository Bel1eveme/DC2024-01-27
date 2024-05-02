using LR1.Dto.Request.CreateTo;
using LR1.Dto.Request.UpdateTo;
using LR1.Dto.Response;

namespace LR1.Services.Interfaces;

public interface ILabelService
{
    Task<LabelResponseTo> GetLabelById(long id);
    Task<IEnumerable<LabelResponseTo>> GetLabels();
    Task<LabelResponseTo> CreateLabel(CreateLabelRequestTo createLabelRequestTo);
    Task DeleteLabel(long id);
    Task<LabelResponseTo> UpdateLabel(UpdateLabelRequestTo modifiedLabel);
}