using Discussion.Kafka;
using Discussion.NoteEntity.Dto;

namespace Discussion.NoteEntity.Kafka
{
    public class NoteKafkaResponse(NoteResponseTO responseTO, State state)
    {
        public NoteResponseTO ResponseTO { get; set; } = responseTO;
        public State State { get; set; } = state;
    }
}
