using DC_Lab1.DTO.Interface;
using DC_Lab1.DTO;
using Microsoft.EntityFrameworkCore;
using DC_Lab1.Services.Interfaces;
using AutoMapper;
using DC_Lab1.Models;
using Microsoft.AspNetCore.Components.Forms;
using DC_Lab1.DB.BaseDBContext;

namespace DC_Lab1.Services
{
    public class PostService(IMapper _mapper, BaseContext dbContext) : IPostService
    {
        public async Task<IResponseTo> CreateEnt(IRequestTo Dto)
        {
            var MessageDto = (PostRequestTo)Dto;

            if (!Validate(MessageDto))
            {
                throw new InvalidDataException("Incorrect data for CREATE Message");

            }
            var Message = _mapper.Map<Post>(MessageDto);
            dbContext.Messages.Add(Message);
            await dbContext.SaveChangesAsync();
            var response = _mapper.Map<PostResponseTo>(Message);
            return response;





        }

        public async Task DeleteEnt(int id)
        {
            try
            {
                var Message = await dbContext.Messages.FindAsync(id);
                dbContext.Messages.Remove(Message!);
                await dbContext.SaveChangesAsync();
                return;
            }
            catch
            {
                throw new Exception("Deletting Message exception");
            }

        }

        public async Task<IResponseTo> GetEntById(int id)
        {

            var Message = _mapper.Map<PostResponseTo>(await dbContext.Messages.FindAsync(id));
            return Message is not null ? _mapper.Map<PostResponseTo>(Message) : throw new ArgumentNullException($"Not found Message: {id}");



        }

        public IEnumerable<IResponseTo> GetAllEnt()
        {
            try
            {
                return dbContext.Messages.Select(_mapper.Map<PostResponseTo>);

            }
            catch
            {
                throw new Exception("Getting all Messages exception");
            }
        }

        public async Task<IResponseTo> UpdateEnt(IRequestTo Dto)
        {
            var MessageDto = (PostRequestTo)Dto;

            if (!Validate(MessageDto))

            {
                throw new InvalidDataException("Incorrect data for UPDATE Message");

            }
            var newMessage = _mapper.Map<Post>(MessageDto);
            dbContext.Messages.Update(newMessage);
            await dbContext.SaveChangesAsync();
            var Message = _mapper.Map<PostResponseTo>(await dbContext.Messages.FindAsync(newMessage.Id));
            return Message;


        }

        public bool Validate(PostRequestTo MessageDto)
        {
            if (MessageDto?.Content?.Length < 2 || MessageDto?.Content?.Length > 2048)
                return false;

            return true;
        }
    }
}
