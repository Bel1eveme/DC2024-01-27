using Publisher.Entity.DTO.RequestTO;
using Publisher.Kafka.Model;

namespace Publisher.Service.Interface
{
    public interface ICommentService
    {
        Task<IList<CommentKafkaResponse>> GetAll();
        Task<CommentKafkaResponse?> Add(CommentRequestTO requestTo);
        Task<bool> Remove(int id);
        Task<CommentKafkaResponse?> Update(CommentRequestTO requestTo);
        Task<CommentKafkaResponse?> GetByID(int id);
    }
}
