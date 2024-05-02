using System.ComponentModel.DataAnnotations;

namespace Forum.Api.Models.Dto;

public class CreatorRequestDto
{
    public long Id { get; set; }

    [Length(2, 64, ErrorMessage = "Wrong login length.")]
    public string Login { get; set; }

    [Length(8, 128, ErrorMessage = "Wrong password length.")]
    public string Password { get; set; }

    [Length(2, 64, ErrorMessage = "Wrong first name length.")]
    public string FirstName { get; set; }

    [Length(2, 64, ErrorMessage = "Wrong last name length.")]
    public string LastName { get; set; }
}