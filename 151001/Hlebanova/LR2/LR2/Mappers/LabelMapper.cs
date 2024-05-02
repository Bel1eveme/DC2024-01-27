using Riok.Mapperly.Abstractions;
using LR2.Dto.Request.CreateTo;
using LR2.Dto.Request.UpdateTo;
using LR2.Dto.Response;
using LR2.Models;

namespace LR2.Mappers;

[Mapper]
public static partial class LabelMapper
{
    public static partial Label Map(UpdateLabelRequestTo updateLabelRequestTo);
    public static partial Label Map(CreateLabelRequestTo createLabelRequestTo);
    public static partial LabelResponseTo Map(Label label);
    public static partial IEnumerable<LabelResponseTo> Map(IEnumerable<Label> labels);

    public static partial IEnumerable<Label> Map(
        IEnumerable<UpdateLabelRequestTo> labelRequestTos);
}