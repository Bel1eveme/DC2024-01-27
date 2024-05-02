using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Service.Interface.Common;

namespace Publisher.Service.Interface
{
    public interface IStickerService : ICrudService<Sticker, StickerRequestTO, StickerResponseTO>
    {
        Task<IList<StickerResponseTO>> GetByTweetID(int tweetId);
    }
}
