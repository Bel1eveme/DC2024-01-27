using Discussion.Kafka;
using Discussion.CommentEntity.Dto;

namespace Discussion.CommentEntity.Kafka
{
    public class CommentKafkaResponse(CommentResponseTO responseTO, State state)
    {
        public CommentResponseTO ResponseTO { get; set; } = responseTO;
        public State State { get; set; } = state;
    }
}
