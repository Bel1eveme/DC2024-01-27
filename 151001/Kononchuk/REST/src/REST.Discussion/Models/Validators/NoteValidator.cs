using FluentValidation;
using REST.Discussion.Models.Entities;

namespace REST.Discussion.Models.Validators;

public class NoteValidator : AbstractValidator<Note>
{
    public NoteValidator()
    {
        RuleFor(editor => editor.Content).MinimumLength(2).MaximumLength(2048);
    }
}