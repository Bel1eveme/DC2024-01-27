using Discussion.Common.Interface;

namespace Discussion.CommentEntity.Interface
{
    public interface ICommentService : ICrudService<Comment, CommentRequestTO, CommentResponseTO>
    {
        Task<IList<Comment>> GetByIssueID(int issueId);
    }
}
