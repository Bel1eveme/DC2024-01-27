using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using DC_Lab1;
using DC_Lab1.Models;
using Microsoft.AspNetCore.Http.HttpResults;
using DC_Lab1.Services.Interfaces;
using DC_Lab1.DTO;
using DC_Lab1.DTO.Interface;


namespace DC_Lab1.Controllers
{
    [ApiController]
    [Route("/api/v1.0/authors")]
    public class AuthorController(IAuthorService AuthorService) : Controller
    {
        [HttpGet]
        public JsonResult GetAuthors()
        {
            try
            {
                var Authors = AuthorService.GetAllEnt();
                return Json(Authors);
            }
            catch 
            {
                Response.StatusCode = 400;

                return Json(BadRequest());

            }

        }

        [HttpGet]
        [Route("{authorId:int}")]
        public async Task<JsonResult> GetAuthorById( int authorId) 
        {
            try
            {
                var Author = await AuthorService.GetEntById(authorId);
                return Json(Author);
            }
            catch 
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }
            

        }

        [HttpPost]
        public async Task<JsonResult> CreateAuthor(AuthorRequestTo AuthorTo)
        {
            try
            {
                Response.StatusCode = 201;
                var Author = await AuthorService.CreateEnt(AuthorTo);
                return Json(Author);
            }
            catch
            {
                Response.StatusCode = 403;
                return Json(BadRequest());
            }
           
        }

        [HttpPut]
        public async Task<JsonResult> UpdateAuthor(AuthorRequestTo AuthorTo)
        {
            IResponseTo newAuthor;
            try
            {
                newAuthor = await AuthorService.UpdateEnt(AuthorTo);
                Response.StatusCode = 200;
                return Json(newAuthor);

            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }
        }

        [HttpDelete("{authorId}")]
        public  async Task<IActionResult> DeleteAuthor(int authorId)
        {
            try
            {
                Response.StatusCode = 204;
                await AuthorService.DeleteEnt(authorId);
            }
            catch
            {
                return NoContent();
                Response.StatusCode = 401;
                return BadRequest();
            }

            return NoContent();
           
            
        }

    }
}
