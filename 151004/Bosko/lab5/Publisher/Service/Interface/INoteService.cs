using Publisher.Entity.DTO.RequestTO;
using Publisher.Kafka.Model;

namespace Publisher.Service.Interface
{
    public interface INoteService
    {
        Task<IList<NoteKafkaResponse>> GetAll();
        Task<NoteKafkaResponse?> Add(NoteRequestTO requestTo);
        Task<bool> Remove(int id);
        Task<NoteKafkaResponse?> Update(NoteRequestTO requestTo);
        Task<NoteKafkaResponse?> GetByID(int id);
    }
}
