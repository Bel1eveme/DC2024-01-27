using FluentValidation;
using Forum.Api.Models.Dto;

namespace Forum.Api.Validation;

public class PostRequestDtoValidator : AbstractValidator<PostRequestDto>
{
    public PostRequestDtoValidator()
    {
        RuleFor(post => post.Content).Length(2, 2048);
    }
}