using DC_Lab1.DTO.Interface;
using System.Text.Json.Serialization;

namespace DC_Lab1.DTO
{
    public class MessageRequestTo : IRequestTo
    {
        public int Id { get; set; }

        public int? StoryId { get; set; }

        public string? Content { get; set; }
    }
}
