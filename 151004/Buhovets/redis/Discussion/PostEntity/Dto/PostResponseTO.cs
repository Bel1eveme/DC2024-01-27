namespace Discussion.CommentEntity.Dto
{
    public record class CommentResponseTO(int Id, int TweetId, string Content, string Country);
}
