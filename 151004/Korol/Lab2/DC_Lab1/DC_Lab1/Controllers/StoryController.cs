using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using DC_Lab1;
using DC_Lab1.Models;
using DC_Lab1.DTO;
using DC_Lab1.Services;
using DC_Lab1.Services.Interfaces;
using DC_Lab1.DTO.Interface;
using Microsoft.AspNetCore.Components.Forms;

namespace DC_Lab1.Controllers
{
    [ApiController]
    [Route("/api/v1.0/storys")]
    public class StoryController(IStoryService StoryService) : Controller
    {
        [HttpGet]
        public JsonResult GetStorys()
        {
            try
            {
                var Storys = StoryService.GetAllEnt();
                return Json(Storys);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());

            }

        }

        [HttpGet]
        [Route("{storyId:int}")]
        public async Task<JsonResult> GetStoryById(int storyId)
        {
            try
            {
                var Story = await StoryService.GetEntById(storyId);
                return Json(Story);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }


        }

        [HttpPost]
        public async Task<JsonResult> CreateStory(StoryRequestTo StoryTo)
        {
            try
            {
                Response.StatusCode = 201;
                var Story = await StoryService.CreateEnt(StoryTo);
                return Json(Story);
            }
            catch
            {
                Response.StatusCode = 403;
                return Json(BadRequest());
            }

        }

        [HttpPut]
        public async Task<JsonResult> UpdateStory(StoryRequestTo StoryTo)
        {
            IResponseTo newStory;
            try
            {
                newStory = await StoryService.UpdateEnt(StoryTo);
                Response.StatusCode = 200;
                return Json(newStory);

            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }
        }

        [HttpDelete("{storyId}")]
        public async Task<IActionResult> DeleteStory(int storyId)
        {
            try
            {
                Response.StatusCode = 204;
                await StoryService.DeleteEnt(storyId);
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
