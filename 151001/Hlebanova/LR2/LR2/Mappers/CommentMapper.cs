using Riok.Mapperly.Abstractions;
using LR2.Dto.Request.CreateTo;
using LR2.Dto.Request.UpdateTo;
using LR2.Dto.Response;
using LR2.Models;

namespace LR2.Mappers;

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