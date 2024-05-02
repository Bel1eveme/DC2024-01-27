using Publisher.Entity.DTO.ResponseTO;

namespace Publisher.Kafka.Model
{
    public class CommentKafkaResponse(CommentResponseTO responseTO, State state)
    {
        public CommentResponseTO ResponseTO { get; set; } = responseTO;
        public State State { get; set; } = state;
    }
}
