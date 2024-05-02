using AutoMapper;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Repository.Interface;
using Publisher.Service.Implementation.Common;
using Publisher.Service.Interface;

namespace Publisher.Service.Implementation
{
    public class EditorService(IMapper mapper, IAuthorRepository repository) :
        AbstractCrudService<Editor, EditorRequestTO, EditorResponseTO>(mapper, repository), IAuthorService
    {
        private readonly IMapper _mapper = mapper;

        public override async Task<EditorResponseTO> Add(EditorRequestTO authorTo)
        {
            if (!Validate(authorTo))
            {
                throw new InvalidDataException("Author is not valid");
            }

            return await base.Add(authorTo);
        }

        public override async Task<EditorResponseTO> Update(EditorRequestTO authorTo)
        {
            if (!Validate(authorTo))
            {
                throw new InvalidDataException($"UPDATE invalid data: {authorTo}");
            }

            return await base.Update(authorTo);
        }

        public async Task<EditorResponseTO> GetByTweetID(int tweetId)
        {
            var response = await repository.GetByTweetIdAsync(tweetId);

            return _mapper.Map<EditorResponseTO>(response.Editor);
        }

        private static bool Validate(EditorRequestTO author)
        {
            var fnameLen = author.FirstName.Length;
            var lnameLen = author.LastName.Length;
            var passLen = author.Password.Length;
            var loginLen = author.Login.Length;

            if (fnameLen < 2 || fnameLen > 64)
            {
                return false;
            }
            if (lnameLen < 2 || fnameLen > 64)
            {
                return false;
            }
            if (passLen < 8 || passLen > 128)
            {
                return false;
            }
            if (loginLen < 2 || loginLen > 64)
            {
                return false;
            }
            return true;
        }
    }
}
