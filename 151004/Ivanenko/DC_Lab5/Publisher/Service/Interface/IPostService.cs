using Publisher.Entity.DTO.RequestTO;
using Publisher.Kafka.Model;

namespace Publisher.Service.Interface
{
    public interface IPostService
    {
        Task<IList<PostKafkaResponse>> GetAll();
        Task<PostKafkaResponse?> Add(PostRequestTO requestTo);
        Task<bool> Remove(int id);
        Task<PostKafkaResponse?> Update(PostRequestTO requestTo);
        Task<PostKafkaResponse?> GetByID(int id);
    }
}
