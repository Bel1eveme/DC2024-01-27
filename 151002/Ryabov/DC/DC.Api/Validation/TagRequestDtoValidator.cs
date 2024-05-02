using FluentValidation;
using Forum.Api.Models.Dto;

namespace Forum.Api.Validation;

public class TagRequestDtoValidator : AbstractValidator<TagRequestDto>
{
    public TagRequestDtoValidator()
    {
        RuleFor(tag => tag.Name).Length(2, 32);
    }
}