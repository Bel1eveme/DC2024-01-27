namespace Publisher.Entity.DTO.ResponseTO
{
    public record class IssueResponseTO(int Id, int CreatorId, string Title, string Content, DateTime Created,
        DateTime Modified);
}
