using FluentValidation;
using Forum.Api.Models.Dto;

namespace Forum.Api.Validation;

public class StorySearchDtoValidator : AbstractValidator<StorySearchDto>
{
    public StorySearchDtoValidator()
    {
        RuleFor(storySearch => storySearch.Title).Length(2, 64);
        RuleFor(storySearch => storySearch.Content).Length(4, 2048);
        RuleFor(storySearch => storySearch.CreatorLogin).Length(2, 64);
    }
}