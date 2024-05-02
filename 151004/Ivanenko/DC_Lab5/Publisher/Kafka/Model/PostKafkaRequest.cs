using Publisher.Entity.DTO.RequestTO;

namespace Publisher.Kafka.Model
{
    public class PostKafkaRequest(PostRequestTO requestTO, HttpMethod method)
    {
        public PostRequestTO RequestTO { get; set; } = requestTO;
        public HttpMethod Method { get; set; } = method;
    }
}
