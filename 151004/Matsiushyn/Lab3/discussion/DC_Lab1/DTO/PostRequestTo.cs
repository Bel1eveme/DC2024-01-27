using DC_Lab1.DTO.Interface;

namespace DC_Lab1.DTO
{
    public class PostRequestTo(int Id,int? tweetId, string? Content) : IRequestTo
    {
        public int Id { get; set; } = Id;
        public int? tweetId { get; set; } = tweetId;

        public string? Content { get; set; } = Content;
    }
}
