using Discussion.Kafka;
using Discussion.PostEntity.Dto;

namespace Discussion.PostEntity.Kafka
{
    public class PostKafkaRequest(PostRequestTO requestTO, HttpMethod method)
    {
        public PostRequestTO RequestTO { get; set; } = requestTO;
        public HttpMethod Method { get; set; } = method;
    }
}
