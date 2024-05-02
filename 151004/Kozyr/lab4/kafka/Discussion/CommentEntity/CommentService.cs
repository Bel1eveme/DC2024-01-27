using AutoMapper;
using Discussion.Common;
using Discussion.CommentEntity.Dto;
using Discussion.CommentEntity.Interface;

namespace Discussion.CommentEntity
{
    public class CommentService(IMapper mapper, ICommentRepository repository)
        : AbstractCrudService<Comment, CommentRequestTO, CommentResponseTO>(mapper, repository), ICommentService
    {
        public override async Task<CommentResponseTO> Add(CommentRequestTO comment)
        {
            if (!Validate(comment))
            {
                throw new InvalidDataException("POST is not valid");
            }

            return await base.Add(comment);
        }

        public override async Task<CommentResponseTO> Update(CommentRequestTO comment)
        {
            if (!Validate(comment))
            {
                throw new InvalidDataException($"UPDATE invalid data: {comment}");
            }

            return await base.Update(comment);
        }

        public Task<IList<Comment>> GetByIssueID(int issueId)
        {
            throw new NotImplementedException();
        }

        private static bool Validate(CommentRequestTO comment)
        {
            var contentLen = comment.Content.Length;

            if (contentLen < 2 || contentLen > 2048)
            {
                return false;
            }
            return true;
        }
    }
}
