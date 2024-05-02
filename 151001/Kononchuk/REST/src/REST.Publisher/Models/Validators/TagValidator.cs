using FluentValidation;
using REST.Publisher.Models.Entities;

namespace REST.Publisher.Models.Validators;

public class TagValidator : AbstractValidator<Tag>
{
    public TagValidator()
    {
        RuleFor(editor => editor.Name).NotEmpty().MinimumLength(2).MaximumLength(32);
    }
}