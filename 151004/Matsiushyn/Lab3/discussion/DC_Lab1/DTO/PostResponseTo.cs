using DC_Lab1.DTO.Interface;

namespace DC_Lab1.DTO
{
    public class PostResponseTo(int Id, int? tweetId, string? Content) : IResponseTo
    {
        public int Id { get; set; } = Id;

        public int? tweetId { get; set; } = tweetId;

        public string? Content { get; set; } = Content;

    }
}
