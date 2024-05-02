using FluentValidation;
using Forum.PostApi.Models.Dto;

namespace Forum.PostApi.Validation;

public class PostRequestDtoValidator : AbstractValidator<PostRequestDto>
{
    public PostRequestDtoValidator()
    {
        RuleFor(post => post.Content).Length(2, 2048);
    }
}