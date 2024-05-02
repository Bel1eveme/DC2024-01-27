using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Service.Interface.Common;

namespace Publisher.Service.Interface
{
    public interface ILabelService : ICrudService<Label, LabelRequestTO, LabelResponseTO>
    {
        Task<IList<LabelResponseTO>> GetByNewsID(int newsId);
    }
}
