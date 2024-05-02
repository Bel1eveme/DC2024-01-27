using Riok.Mapperly.Abstractions;
using LR1.Dto.Request.CreateTo;
using LR1.Dto.Request.UpdateTo;
using LR1.Dto.Response;
using LR1.Models;

namespace LR1.Mappers;

[Mapper]
public static partial class CreatorMapper
{
    public static partial Creator Map(UpdateCreatorRequestTo updateCreatorRequestTo);
    public static partial Creator Map(CreateCreatorRequestTo createCreatorRequestTo);
    public static partial CreatorResponseTo Map(Creator creator);
    public static partial IEnumerable<CreatorResponseTo> Map(IEnumerable<Creator> creators);

    public static partial IEnumerable<Creator> Map(
        IEnumerable<UpdateCreatorRequestTo> creatorRequestTos);
}