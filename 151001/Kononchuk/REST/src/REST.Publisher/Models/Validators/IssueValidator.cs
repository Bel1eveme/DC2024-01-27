using FluentValidation;
using REST.Publisher.Models.Entities;

namespace REST.Publisher.Models.Validators;

public class IssueValidator : AbstractValidator<Issue>
{
    public IssueValidator()
    {
        RuleFor(editor => editor.Title).NotEmpty().MinimumLength(2).MaximumLength(64);
        RuleFor(editor => editor.Content).MinimumLength(4).MaximumLength(2048);
    }
}