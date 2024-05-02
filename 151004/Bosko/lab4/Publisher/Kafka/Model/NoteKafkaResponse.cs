using Publisher.Entity.DTO.ResponseTO;

namespace Publisher.Kafka.Model
{
    public class NoteKafkaResponse(NoteResponseTO responseTO, State state)
    {
        public NoteResponseTO ResponseTO { get; set; } = responseTO;
        public State State { get; set; } = state;
    }
}
