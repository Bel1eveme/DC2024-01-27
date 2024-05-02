using LR2.Dto.Request.CreateTo;
using LR2.Dto.Request.UpdateTo;
using LR2.Dto.Response;

namespace LR2.Services.Interfaces;

public interface ICommentService
{
    Task<CommentResponseTo> GetCommentById(long id);
    Task<IEnumerable<CommentResponseTo>> GetComments();
    Task<CommentResponseTo> CreateComment(CreateCommentRequestTo createCommentRequestTo);
    Task DeleteComment(long id);
    Task<CommentResponseTo> UpdateComment(UpdateCommentRequestTo modifiedComment);
}