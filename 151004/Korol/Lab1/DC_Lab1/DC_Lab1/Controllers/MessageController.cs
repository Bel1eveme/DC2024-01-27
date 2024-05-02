using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using DC_Lab1;
using DC_Lab1.Models;
using DC_Lab1.Services.Interfaces;
using DC_Lab1.DTO.Interface;
using DC_Lab1.DTO;
using DC_Lab1.Services;
using Microsoft.AspNetCore.Components.Forms;

namespace DC_Lab1.Controllers
{
    [ApiController]
    [Route("/api/v1.0/messages")]
    public class MessageController(IMessageService MessageService) : Controller
    {
        [HttpGet]
        public JsonResult GetMessages()
        {
            try
            {
                var Messages = MessageService.GetAllEnt();
                return Json(Messages);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());

            }
        }

        [HttpGet]
        [Route("{messageId:int}")]
        public async Task<JsonResult> GetMessageById(int messageId)
        {
            try
            {
                var Message = await MessageService.GetEntById(messageId);
                return Json(Message);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }
        }

        [HttpPost]
        public async Task<JsonResult> CreateMessage(MessageRequestTo MessageTo)
        {
            try
            {
                Response.StatusCode = 201;
                var Message = await MessageService.CreateEnt(MessageTo);
                return Json(Message);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }
        }

        [HttpPut]
        public async Task<JsonResult> UpdateMessage(MessageRequestTo MessageTo)
        {
            IResponseTo newMessage;
            try
            {
                newMessage = await MessageService.UpdateEnt(MessageTo);
                Response.StatusCode = 200;
                return Json(newMessage);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }
        }

        [HttpDelete("{messageId}")]
        public async Task<IActionResult> DeleteMessage(int messageId)
        {
            try
            {
                Response.StatusCode = 204;
                await MessageService.DeleteEnt(messageId);
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
