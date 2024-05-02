namespace Publisher.Entity.DTO.RequestTO
{
    public record class NewsRequestTO(int Id, int UserId, string Title, string Content, DateTime Created, DateTime Modified);
}
