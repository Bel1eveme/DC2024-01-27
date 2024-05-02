using AutoMapper;
using DC_Lab1.DB.BaseDBContext;
using DC_Lab1.DTO;
using DC_Lab1.DTO.Interface;
using DC_Lab1.Models;
using DC_Lab1.Services.Interfaces;
using Humanizer;
using Microsoft.EntityFrameworkCore;
using System.Globalization;
using static System.Net.Mime.MediaTypeNames;

namespace DC_Lab1.Services
{
    public class StoryService(IMapper _mapper, BaseContext dbContext) : IStoryService
    {
        public async Task<IResponseTo> CreateEnt(IRequestTo Dto)
        {
            var StoryDto = (StoryRequestTo)Dto;
            if (!Validate(StoryDto))
            {
                throw new InvalidDataException("Incorrect data for CREATE Story");
            }
            var Story = _mapper.Map<Story>(StoryDto);
            string time = DateTime.UtcNow.ToString("o", CultureInfo.InvariantCulture);
            Story.Created = time;
            Story.Modified = time;
            dbContext.Storys.Add(Story);
            await dbContext.SaveChangesAsync();
            var response = _mapper.Map<StoryResponseTo>(Story);
            return response;
        }

        public async Task DeleteEnt(int id)
        {
            try
            {
                var Story = await dbContext.Storys.FindAsync(id);
                dbContext.Storys.Remove(Story!);
                await dbContext.SaveChangesAsync();
                return;
            }
            catch
            {
                throw new Exception("Deletting Story exception");
            }
        }

        public async Task<IResponseTo> GetEntById(int id)
        {
            var Story = _mapper.Map<StoryResponseTo>(await dbContext.Storys.FindAsync(id));
            return Story is not null ? _mapper.Map<StoryResponseTo>(Story) : throw new ArgumentNullException($"Not found Story: {id}");
        }

        public IEnumerable<IResponseTo> GetAllEnt()
        {
            try
            {
                return dbContext.Storys.Select(_mapper.Map<StoryResponseTo>);
            }
            catch
            {
                throw new Exception("Gettting all Storys exception");
            }
        }

        public async Task<IResponseTo> UpdateEnt(IRequestTo Dto)
        {
            var StoryDto = (StoryRequestTo)Dto;

            if (!Validate(StoryDto))
            {
                throw new InvalidDataException("Incorrect data for UPDATE Story");
            }
            var newStory = _mapper.Map<Story>(StoryDto);
            string time = DateTime.UtcNow.ToString("o", CultureInfo.InvariantCulture);
            newStory.Modified = time;
            dbContext.Storys.Update(newStory);
            await dbContext.SaveChangesAsync();
            var Story = _mapper.Map<StoryResponseTo>(await dbContext.Storys.FindAsync(newStory.Id));
            return Story;
        }

        public bool Validate(StoryRequestTo StoryDto)
        {
            if (StoryDto?.Title?.Length < 2 || StoryDto?.Title?.Length > 64)
                return false;
            if (StoryDto?.Content?.Length < 8 || StoryDto?.Content?.Length > 2048)
                return false;

            return true;
        }
    }
}
