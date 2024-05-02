using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Service.Interface.Common;

namespace Publisher.Service.Interface
{
    public interface ITweetService : ICrudService<Tweet, TweetRequestTO, TweetResponseTO>
    {
        Task<TweetResponseTO> GetTweetByParam(IList<string> StickerNames, IList<int> StickerIds, string EditorLogin,
            string title, string content);
    }
}
