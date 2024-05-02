using FluentValidation;
using Forum.Api.Models.Dto;

namespace Forum.Api.Validation;

public class StoryRequestDtoValidator : AbstractValidator<StoryRequestDto>
{
    public StoryRequestDtoValidator()
    {
        RuleFor(story => story.Title).Length(2, 64);
        RuleFor(story => story.Content).Length(4, 2048);
    }
}