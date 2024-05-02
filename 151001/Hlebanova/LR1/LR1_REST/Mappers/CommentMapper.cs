using Riok.Mapperly.Abstractions;
using LR1.Dto.Request.CreateTo;
using LR1.Dto.Request.UpdateTo;
using LR1.Dto.Response;
using LR1.Models;

namespace LR1.Mappers;

[Mapper]
public static partial class CommentMapper
{
    public static partial Comment Map(UpdateCommentRequestTo updateCommentRequestTo);
    public static partial Comment Map(CreateCommentRequestTo createCommentRequestTo);
    public static partial CommentResponseTo Map(Comment comment);
    public static partial IEnumerable<CommentResponseTo> Map(IEnumerable<Comment> comments);

    public static partial IEnumerable<Comment> Map(
        IEnumerable<UpdateCommentRequestTo> commentRequestTos);
}