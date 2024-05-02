using FluentValidation;
using Publisher.DTO.RequestDTO;

namespace Publisher.Infrastructure.Validators;

public class IssueRequestDtoValidator : AbstractValidator<IssueRequestDto>
{
	public IssueRequestDtoValidator()
	{
		RuleFor(dto => dto.Title).Length(2, 64);
		RuleFor(dto => dto.Content).Length(4, 2048);
	}
}