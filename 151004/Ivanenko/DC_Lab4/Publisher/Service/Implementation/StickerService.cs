using AutoMapper;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Repository.Interface;
using Publisher.Service.Implementation.Common;
using Publisher.Service.Interface;

namespace Publisher.Service.Implementation
{
    public class StickerService(IMapper mapper, IStickerRepository repository)
        : AbstractCrudService<Sticker, StickerRequestTO, StickerResponseTO>(mapper, repository), IStickerService
    {
        public override async Task<StickerResponseTO> Add(StickerRequestTO Sticker)
        {
            if (!Validate(Sticker))
            {
                throw new InvalidDataException("Sticker is not valid");
            }

            return await base.Add(Sticker);
        }

        public override async Task<StickerResponseTO> Update(StickerRequestTO Sticker)
        {
            if (!Validate(Sticker))
            {
                throw new InvalidDataException($"UPDATE invalid data: {Sticker}");
            }

            return await base.Update(Sticker);
        }

        public Task<IList<StickerResponseTO>> GetByTweetID(int tweetId)
        {
            throw new NotImplementedException();
        }

        private static bool Validate(StickerRequestTO Sticker)
        {
            var nameLen = Sticker.Name.Length;

            if (nameLen < 2 || nameLen > 32)
            {
                return false;
            }
            return true;
        }
    }
}
