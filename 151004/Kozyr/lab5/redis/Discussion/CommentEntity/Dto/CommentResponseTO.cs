namespace Discussion.CommentEntity.Dto
{
    public record class CommentResponseTO(int Id, int IssueId, string Content, string Country);
}
