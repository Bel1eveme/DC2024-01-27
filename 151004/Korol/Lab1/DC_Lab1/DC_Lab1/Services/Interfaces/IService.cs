using DC_Lab1.DTO;
using DC_Lab1.DTO.Interface;

namespace DC_Lab1.Services.Interfaces
{
    public interface IService
    {
        public IEnumerable<IResponseTo> GetAllEnt();
        public Task<IResponseTo> GetEntById(int id);
        public Task<IResponseTo> CreateEnt(IRequestTo RequestDto);
        public Task<IResponseTo> UpdateEnt(IRequestTo RequestDto);
        public Task DeleteEnt(int id);
    }
}
