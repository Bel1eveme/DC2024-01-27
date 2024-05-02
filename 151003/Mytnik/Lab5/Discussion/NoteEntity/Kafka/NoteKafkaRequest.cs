using Discussion.Kafka;
using Discussion.NoteEntity.Dto;

namespace Discussion.NoteEntity.Kafka
{
    public class NoteKafkaRequest(NoteRequestTO requestTO, HttpMethod method)
    {
        public NoteRequestTO RequestTO { get; set; } = requestTO;
        public HttpMethod Method { get; set; } = method;
    }
}
