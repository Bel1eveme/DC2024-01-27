using Publisher.Entity.DTO.RequestTO;

namespace Publisher.Kafka.Model
{
    public class CommentKafkaRequest(CommentRequestTO requestTO, HttpMethod method)
    {
        public CommentRequestTO RequestTO { get; set; } = requestTO;
        public HttpMethod Method { get; set; } = method;
    }
}
