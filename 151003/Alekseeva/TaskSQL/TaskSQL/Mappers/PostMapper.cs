using Riok.Mapperly.Abstractions;
using TaskSQL.Dto.Request.CreateTo;
using TaskSQL.Dto.Request.UpdateTo;
using TaskSQL.Dto.Response;
using TaskSQL.Models;

namespace TaskSQL.Mappers;

[Mapper]
public static partial class PostMapper
{
    public static partial Post Map(UpdatePostRequestTo updatePostRequestTo);
    public static partial Post Map(CreatePostRequestTo createPostRequestTo);
    public static partial PostResponseTo Map(Post post);
    public static partial IEnumerable<PostResponseTo> Map(IEnumerable<Post> posts);

    public static partial IEnumerable<Post> Map(
        IEnumerable<UpdatePostRequestTo> postRequestTos);
}