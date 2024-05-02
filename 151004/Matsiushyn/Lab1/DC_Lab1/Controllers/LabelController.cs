using DC_Lab1.DTO.Interface;
using DC_Lab1.DTO;
using DC_Lab1.Services;
using DC_Lab1.Services.Interfaces;
using Microsoft.AspNetCore.Mvc;

namespace DC_Lab1.Controllers
{
    [ApiController]
    [Route("/api/v1.0/labels")]
    public class LabelController(ILabelService LabelService) : Controller
    {
        [HttpGet]
        public JsonResult GetLabels()
        {
            try
            {
                var Labels = LabelService.GetAllEnt();
                return Json(Labels);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());

            }

        }

        [HttpGet]
        [Route("{labelId:int}")]
        public async Task<JsonResult> GetLabelById(int labelId)
        {
            try
            {
                var Label = await LabelService.GetEntById(labelId);
                return Json(Label);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }


        }

        [HttpPost]
        public async Task<JsonResult> CreateLabel(LabelRequestTo LabelTo)
        {
            try
            {
                Response.StatusCode = 201;
                var Label = await LabelService.CreateEnt(LabelTo);
                return Json(Label);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }

        }

        [HttpPut]
        public async Task<JsonResult> UpdateLabel(LabelRequestTo LabelTo)
        {
            IResponseTo newLabel;
            try
            {
                newLabel = await LabelService.UpdateEnt(LabelTo);
                Response.StatusCode = 200;
                return Json(newLabel);

            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }
        }

        [HttpDelete("{labelId}")]
        public async Task<IActionResult> DeleteLabel(int labelId)
        {
            try
            {
                Response.StatusCode = 204;
                await LabelService.DeleteEnt(labelId);
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
