using FluentValidation;
using Forum.Api.Models.Dto;

namespace Forum.Api.Validation;

public class CreatorRequestDtoValidator : AbstractValidator<CreatorRequestDto>
{
    public CreatorRequestDtoValidator()
    {
        RuleFor(creator => creator.Login).Length(2, 64);
        RuleFor(creator => creator.Password).Length(8, 128);
        RuleFor(creator => creator.FirstName).Length(2, 64);
        RuleFor(creator => creator.LastName).Length(2, 64);
    }
}