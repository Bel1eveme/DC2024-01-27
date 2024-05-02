namespace Publisher.Entity.DTO.RequestTO
{
    public record class IssueRequestTO(int Id, int CreatorId, string Title, string Content, DateTime Created, DateTime Modified);
}
