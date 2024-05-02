using Publisher.Entity.DTO.ResponseTO;

namespace Publisher.Kafka.Model
{
    public class PostKafkaResponse(PostResponseTO responseTO, State state)
    {
        public PostResponseTO ResponseTO { get; set; } = responseTO;
        public State State { get; set; } = state;
    }
}
