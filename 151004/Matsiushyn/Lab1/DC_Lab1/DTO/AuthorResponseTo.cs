using DC_Lab1.DTO.Interface;
using DC_Lab1.Models;

namespace DC_Lab1.DTO
{
    public class AuthorResponseTo(int Id, string? Login, string? Password, string? Firstname, string? Lastname, ICollection<Tweet> Tweets) : IResponseTo
    {
        public int Id { get; set; } = Id;

        public string? Login { get; set; } = Login;

        public string? Password { get; set; } =    Password;

        public string? Firstname { get; set; } = Firstname;

        public string? Lastname { get; set; } = Lastname;

        public virtual ICollection<Tweet> Tweets { get; set; } = Tweets;
    }
}
