using DC_Lab1.DTO.Interface;
using DC_Lab1.DTO;
using DC_Lab1.Services;
using DC_Lab1.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DC_Lab1.Controllers
{
    [ApiController]
    [Route("/api/v1.0/stickers")]
    public class StickerController(IStickerService StickerService) : Controller
    {
        [HttpGet]
        public JsonResult GetStickers()
        {
            try
            {
                var Stickers = StickerService.GetAllEnt();
                return Json(Stickers);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());

            }

        }

        [HttpGet]
        [Route("{stickerId:int}")]
        public async Task<JsonResult> GetStickerById(int stickerId)
        {
            try
            {
                var Sticker = await StickerService.GetEntById(stickerId);
                return Json(Sticker);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }


        }

        [HttpPost]
        public async Task<JsonResult> CreateSticker(StickerRequestTo StickerTo)
        {
            try
            {
                Response.StatusCode = 201;
                var Sticker = await StickerService.CreateEnt(StickerTo);
                return Json(Sticker);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }

        }

        [HttpPut]
        public async Task<JsonResult> UpdateSticker(StickerRequestTo StickerTo)
        {
            IResponseTo newSticker;
            try
            {
                newSticker = await StickerService.UpdateEnt(StickerTo);
                Response.StatusCode = 200;
                return Json(newSticker);

            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }
        }

        [HttpDelete("{stickerId}")]
        public async Task<IActionResult> DeleteSticker(int stickerId)
        {
            try
            {
                Response.StatusCode = 204;
                await StickerService.DeleteEnt(stickerId);
            }
            catch
            {
                Response.StatusCode = 401;
                return BadRequest();
            }

            return NoContent();


        }
    }
}
