namespace Publisher.Entity.DTO.ResponseTO
{
    public record class TweetResponseTO(int Id, int EditorId, string Title, string Content, DateTime Created,
        DateTime Modified);
}
