using DC_Lab1.DTO.Interface;
using DC_Lab1.Models;

namespace DC_Lab1.DTO
{
    public class StoryResponseTo(int Id, int? authorId, string? Title, string? Content, string? Created, string? Modified ,ICollection<Post> Messages, Author? Author) : IResponseTo
    {
        public int Id { get; set; } = Id;

        public int? authorId { get; set; } = authorId;

        public string? Title { get; set; } = Title;

        public string? Content { get; set; } = Content;

        public string? Created { get; set; } = Created;

        public string? Modified { get; set; } = Modified;

        public virtual ICollection<Post> Messages { get; set; } = Messages;

        public virtual Author? Author { get; set; } = Author;
    }
}
