using Publisher.Entity.DTO.RequestTO;

namespace Publisher.Kafka.Model
{
    public class NoteKafkaRequest(NoteRequestTO requestTO, HttpMethod method)
    {
        public NoteRequestTO RequestTO { get; set; } = requestTO;
        public HttpMethod Method { get; set; } = method;
    }
}
