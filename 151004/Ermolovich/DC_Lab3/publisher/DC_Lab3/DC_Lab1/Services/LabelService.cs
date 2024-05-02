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
    public class LabelService(IMapper _mapper, BaseContext dbContext) : IStickerService
    {
        public async Task<IResponseTo> CreateEnt(IRequestTo Dto)
        {
            var StickerDto = (LabelRequestTo)Dto;

            if (!Validate(StickerDto))
            {
                throw new InvalidDataException("Incorrect data for CREATE Sticker");

            }

            var Sticker = _mapper.Map<Label>(StickerDto);
            dbContext.Stickers.Add(Sticker);
            await dbContext.SaveChangesAsync();
            var response = _mapper.Map<LabelResponseTo>(Sticker);
            return response;
        }

        public async Task DeleteEnt(int id)
        {
            try
            {
                var Sticker = await dbContext.Stickers.FindAsync(id);
                dbContext.Stickers.Remove(Sticker!);
                await dbContext.SaveChangesAsync();
                return;
            }
            catch
            {
                throw new Exception("Deletting Sticker exception");
            }
        }

        public async Task<IResponseTo> GetEntById(int id)
        {

            var Sticker = _mapper.Map<LabelResponseTo>(await dbContext.Stickers.FindAsync(id));
            return Sticker is not null ? _mapper.Map<LabelResponseTo>(Sticker) : throw new ArgumentNullException($"Not found Sticker: {id}");

        }

        public IEnumerable<IResponseTo> GetAllEnt()
        {
            try
            {
                return dbContext.Stickers.Select(_mapper.Map<LabelResponseTo>);

            }
            catch
            {
                throw new Exception("Getting all strickers exception");
            }
        }

        public async Task<IResponseTo> UpdateEnt(IRequestTo Dto)
        {
            var StickerDto = (LabelRequestTo)Dto;

            if (!Validate(StickerDto))
            {
                throw new InvalidDataException("Incorrect data for UPDATE Sticker");


            }
            var newSticker = _mapper.Map<Label>(StickerDto);
            dbContext.Stickers.Update(newSticker);
            await dbContext.SaveChangesAsync();
            var Sticker = _mapper.Map<LabelResponseTo>(await dbContext.Stickers.FindAsync(newSticker.Id));
            return Sticker;


        }

        public bool Validate(LabelRequestTo dto)
        {
            if (dto.name?.Length < 2 || dto.name?.Length > 32)
                return false;
            return true;
        }


    }
}
