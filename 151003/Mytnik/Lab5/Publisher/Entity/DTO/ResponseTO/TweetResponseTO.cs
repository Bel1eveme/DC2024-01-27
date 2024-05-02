namespace Publisher.Entity.DTO.ResponseTO
{
    public record class TweetResponseTO(int Id, int CreatorId, string Title, string Content, DateTime Created,
        DateTime Modified);
}
