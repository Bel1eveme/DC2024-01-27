using DC_Lab1.DTO.Interface;
using DC_Lab1.Models;

namespace DC_Lab1.DTO
{
    public class StoryRequestTo(int Id, int? authorId, string? Title, string? Content) : IRequestTo
    {
        public int Id { get; set; } = Id;

        public int? authorId { get; set; } = authorId;

        public string? Title { get; set; } = Title;

        public string? Content { get; set; } = Content;


    }
}
