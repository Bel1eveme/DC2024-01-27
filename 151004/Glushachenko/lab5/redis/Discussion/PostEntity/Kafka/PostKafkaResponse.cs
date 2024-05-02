using Discussion.Kafka;
using Discussion.PostEntity.Dto;

namespace Discussion.PostEntity.Kafka
{
    public class PostKafkaResponse(PostResponseTO responseTO, State state)
    {
        public PostResponseTO ResponseTO { get; set; } = responseTO;
        public State State { get; set; } = state;
    }
}
