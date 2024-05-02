using Riok.Mapperly.Abstractions;
using LR1.Dto.Request.CreateTo;
using LR1.Dto.Request.UpdateTo;
using LR1.Dto.Response;
using LR1.Models;

namespace LR1.Mappers;

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