using FluentValidation;
using REST.Publisher.Models.Entities;

namespace REST.Publisher.Models.Validators;

public class EditorValidator: AbstractValidator<Editor>
{
    public EditorValidator()
    {
        RuleFor(editor => editor.Login).NotEmpty().MinimumLength(2).MaximumLength(64);
        RuleFor(editor => editor.Password).NotEmpty().MinimumLength(8).MaximumLength(128);
        RuleFor(editor => editor.FirstName).NotEmpty().MinimumLength(2).MaximumLength(64);
        RuleFor(editor => editor.LastName).NotEmpty().MinimumLength(2).MaximumLength(64);
    }
}