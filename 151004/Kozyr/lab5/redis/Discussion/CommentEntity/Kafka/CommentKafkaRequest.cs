using Discussion.Kafka;
using Discussion.CommentEntity.Dto;

namespace Discussion.CommentEntity.Kafka
{
    public class CommentKafkaRequest(CommentRequestTO requestTO, HttpMethod method)
    {
        public CommentRequestTO RequestTO { get; set; } = requestTO;
        public HttpMethod Method { get; set; } = method;
    }
}
