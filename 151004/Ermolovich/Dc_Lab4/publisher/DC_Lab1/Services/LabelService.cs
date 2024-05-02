using DC_Lab1.DTO.Interface;
using DC_Lab1.DTO;
using DC_Lab1.Services.Interfaces;
using Microsoft.EntityFrameworkCore;
using System.Globalization;
using AutoMapper;
using DC_Lab1.Models;
using Microsoft.Extensions.Hosting;
using DC_Lab1.DB.BaseDBContext;

namespace DC_Lab1.Services
{
    public class LabelService(IMapper _mapper, BaseContext dbContext) : ILabelService
    {
        public async Task<IResponseTo> CreateEnt(IRequestTo Dto)
        {
            var LabelDto = (LabelRequestTo)Dto;

            if (!Validate(LabelDto))
            {
                throw new InvalidDataException("Incorrect data for CREATE Label");

            }

            var Label = _mapper.Map<Label>(LabelDto);
            dbContext.Labels.Add(Label);
            await dbContext.SaveChangesAsync();
            var response = _mapper.Map<LabelResponseTo>(Label);
            return response;
        }

        public async Task DeleteEnt(int id)
        {
            try
            {
                var Label = await dbContext.Labels.FindAsync(id);
                dbContext.Labels.Remove(Label!);
                await dbContext.SaveChangesAsync();
                return;
            }
            catch
            {
                throw new Exception("Deletting Label exception");
            }
        }

        public async Task<IResponseTo> GetEntById(int id)
        {

            var Label = _mapper.Map<LabelResponseTo>(await dbContext.Labels.FindAsync(id));
            return Label is not null ? _mapper.Map<LabelResponseTo>(Label) : throw new ArgumentNullException($"Not found Label: {id}");

        }

        public IEnumerable<IResponseTo> GetAllEnt()
        {
            try
            {
                return dbContext.Labels.Select(_mapper.Map<LabelResponseTo>);

            }
            catch
            {
                throw new Exception("Getting all strickers exception");
            }
        }

        public async Task<IResponseTo> UpdateEnt(IRequestTo Dto)
        {
            var LabelDto = (LabelRequestTo)Dto;

            if (!Validate(LabelDto))
            {
                throw new InvalidDataException("Incorrect data for UPDATE Label");


            }
            var newLabel = _mapper.Map<Label>(LabelDto);
            dbContext.Labels.Update(newLabel);
            await dbContext.SaveChangesAsync();
            var Label = _mapper.Map<LabelResponseTo>(await dbContext.Labels.FindAsync(newLabel.Id));
            return Label;


        }

        public bool Validate(LabelRequestTo dto)
        {
            if (dto.name?.Length < 2 || dto.name?.Length > 32)
                return false;
            return true;
        }


    }
}
