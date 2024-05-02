using Riok.Mapperly.Abstractions;
using LR2.Dto.Request.CreateTo;
using LR2.Dto.Response;
using LR2.Models;

namespace LR2.Mappers;

[Mapper]
public static partial class CreatorMapper
{
    public static partial Creator ToEntity(this CreateCreatorRequestTo createCreatorRequestTo);
    public static partial CreatorResponseTo ToResponse(this Creator creator);
    public static partial Creator Map(CreateCreatorRequestTo createCreatorRequestTo);
    public static partial CreatorResponseTo Map(Creator creator);
    public static partial IEnumerable<CreatorResponseTo> Map(IEnumerable<Creator> creators);
}