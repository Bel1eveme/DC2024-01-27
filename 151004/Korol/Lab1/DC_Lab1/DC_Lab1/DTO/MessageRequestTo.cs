using DC_Lab1.DTO.Interface;

namespace DC_Lab1.DTO
{
    public class MessageRequestTo(int Id,int? storyId, string? Content) : IRequestTo
    {
        public int Id { get; set; } = Id;
        public int? storyId { get; set; } = storyId;
        public string? Content { get; set; } = Content;
    }
}
