using AutoMapper;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Repository.Interface;
using Publisher.Service.Implementation.Common;
using Publisher.Service.Interface;

namespace Publisher.Service.Implementation
{
    public class EditorService(IMapper mapper, IEditorRepository repository) :
        AbstractCrudService<Editor, EditorRequestTO, EditorResponseTO>(mapper, repository), IEditorService
    {
        private readonly IMapper _mapper = mapper;

        public override async Task<EditorResponseTO> Add(EditorRequestTO EditorTo)
        {
            if (!Validate(EditorTo))
            {
                throw new InvalidDataException("Editor is not valid");
            }

            return await base.Add(EditorTo);
        }

        public override async Task<EditorResponseTO> Update(EditorRequestTO EditorTo)
        {
            if (!Validate(EditorTo))
            {
                throw new InvalidDataException($"UPDATE invalid data: {EditorTo}");
            }

            return await base.Update(EditorTo);
        }

        public async Task<EditorResponseTO> GetByTweetID(int tweetId)
        {
            var response = await repository.GetByTweetIdAsync(tweetId);

            return _mapper.Map<EditorResponseTO>(response.Editor);
        }

        private static bool Validate(EditorRequestTO Editor)
        {
            var fnameLen = Editor.FirstName.Length;
            var lnameLen = Editor.LastName.Length;
            var passLen = Editor.Password.Length;
            var loginLen = Editor.Login.Length;

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
