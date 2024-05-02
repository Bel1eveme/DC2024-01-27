namespace Discussion.CommentEntity.Dto
{
    public record class CommentRequestTO(int Id, int TweetId, string Content);
}
