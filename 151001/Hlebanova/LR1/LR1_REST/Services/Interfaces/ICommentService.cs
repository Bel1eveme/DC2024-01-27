using LR1.Dto.Request.CreateTo;
using LR1.Dto.Request.UpdateTo;
using LR1.Dto.Response;

namespace LR1.Services.Interfaces;

public interface ICommentService
{
    Task<CommentResponseTo> GetCommentById(long id);
    Task<IEnumerable<CommentResponseTo>> GetComments();
    Task<CommentResponseTo> CreateComment(CreateCommentRequestTo createCommentRequestTo);
    Task DeleteComment(long id);
    Task<CommentResponseTo> UpdateComment(UpdateCommentRequestTo modifiedComment);
}