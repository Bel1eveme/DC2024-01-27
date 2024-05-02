using FluentValidation;
using Publisher.DTO.RequestDTO;

namespace Publisher.Infrastructure.Validators;

public class NoteRequestDtoValidator : AbstractValidator<PostRequestDto>
{
	public NoteRequestDtoValidator()
	{
		RuleFor(dto => dto.Content).Length(2, 2048);
	}
}