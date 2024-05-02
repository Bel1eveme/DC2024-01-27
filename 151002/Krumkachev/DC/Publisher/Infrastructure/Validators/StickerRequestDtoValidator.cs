using FluentValidation;
using Publisher.DTO.RequestDTO;

namespace Publisher.Infrastructure.Validators;

public class LabelRequestDtoValidator : AbstractValidator<LabelRequestDto>
{
	public LabelRequestDtoValidator()
	{
		RuleFor(dto => dto.Name).Length(2, 32);
	}
}