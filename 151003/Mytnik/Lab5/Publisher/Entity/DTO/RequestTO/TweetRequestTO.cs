namespace Publisher.Entity.DTO.RequestTO
{
    public record class TweetRequestTO(int Id, int CreatorId, string Title, string Content, DateTime Created, DateTime Modified);
}
