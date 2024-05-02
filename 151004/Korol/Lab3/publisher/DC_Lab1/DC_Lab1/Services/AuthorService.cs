using DC_Lab1.DTO;
using DC_Lab1.Services.Interfaces;
using DC_Lab1.Models;
using AutoMapper; 
using Microsoft.EntityFrameworkCore;
using DC_Lab1.DTO.Interface;
using Microsoft.Data.Sqlite;
using DC_Lab1.DB.BaseDBContext;

namespace DC_Lab1.Services
{
    public class AuthorService(IMapper _mapper, BaseContext dbContext) : IAuthorService
    {
        public async Task<IResponseTo> CreateEnt(IRequestTo Dto)
        {
            var AuthorDto = (AuthorRequestTo)Dto;
            if (!Validate(AuthorDto))
            {
                throw new InvalidDataException("Incorrect data for CREATE Author");
            }
            var Author = _mapper.Map<Author>(AuthorDto);
            dbContext.Add(Author);
            await dbContext.SaveChangesAsync();
            var response = _mapper.Map<AuthorResponseTo>(Author);
            return response;
        }

        public async Task DeleteEnt(int id)
        {
            try
            {
                var Author = await dbContext.Authors.FindAsync(id);
                dbContext.Authors.Remove(Author!);
                await dbContext.SaveChangesAsync();
                return;
            }
            catch
            {
                throw new Exception("Deleting Author exception");
            }
        }

        public async Task<IResponseTo> GetEntById(int id)
        {
            var Author = await dbContext.Authors.FindAsync(id);
            return Author is not null ? _mapper.Map<AuthorResponseTo>(Author) : throw new ArgumentNullException($"Not found Author: {id}");
        }

        public IEnumerable<IResponseTo> GetAllEnt()
        {
            try
            {
                return dbContext.Authors.Select(_mapper.Map<AuthorResponseTo>);
            }
            catch
            {
                throw new Exception("Getting all Authors exception");
            }
        }

        public async Task<IResponseTo> UpdateEnt(IRequestTo Dto)
        {
            var AuthorDto = (AuthorRequestTo)Dto;
            if (!Validate(AuthorDto))
            {
                throw new InvalidDataException("Incorrect data for UPDATE");         
            }
            var newAuthor = _mapper.Map<Author>(AuthorDto);
            dbContext.Authors.Update(newAuthor);
            await dbContext.SaveChangesAsync();
            var Author = _mapper.Map<AuthorResponseTo>(await dbContext.Authors.FindAsync(newAuthor.Id));
            return Author;
        }

        public bool Validate(AuthorRequestTo AuthorDto)
        {
            if (AuthorDto?.Login?.Length < 2 || AuthorDto?.Login?.Length > 64)
                return false;
            if (AuthorDto?.Lastname?.Length < 2 || AuthorDto?.Lastname?.Length > 64)
                return false;
            if (AuthorDto?.Firstname?.Length < 2 || AuthorDto?.Firstname?.Length > 64)
                return false;
            if (AuthorDto?.Password?.Length < 8 || AuthorDto?.Password?.Length > 128)
                return false;
            return true;
        }

    }
}
