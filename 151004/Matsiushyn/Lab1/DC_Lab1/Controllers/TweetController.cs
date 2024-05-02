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
    [Route("/api/v1.0/tweets")]
    public class TweetController(ITweetService TweetService) : Controller
    {
        [HttpGet]
        public JsonResult GetTweets()
        {
            try
            {
                var Tweets = TweetService.GetAllEnt();
                return Json(Tweets);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());

            }

        }

        [HttpGet]
        [Route("{tweetId:int}")]
        public async Task<JsonResult> GetTweetById(int tweetId)
        {
            try
            {
                var Tweet = await TweetService.GetEntById(tweetId);
                return Json(Tweet);
            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }


        }

        [HttpPost]
        public async Task<JsonResult> CreateTweet(TweetRequestTo TweetTo)
        {
            try
            {
                Response.StatusCode = 201;
                var Tweet = await TweetService.CreateEnt(TweetTo);
                return Json(Tweet);
            }
            catch
            {
                Response.StatusCode = 403;
                return Json(BadRequest());
            }

        }

        [HttpPut]
        public async Task<JsonResult> UpdateTweet(TweetRequestTo TweetTo)
        {
            IResponseTo newTweet;
            try
            {
                newTweet = await TweetService.UpdateEnt(TweetTo);
                Response.StatusCode = 200;
                return Json(newTweet);

            }
            catch
            {
                Response.StatusCode = 400;
                return Json(BadRequest());
            }
        }

        [HttpDelete("{tweetId}")]
        public async Task<IActionResult> DeleteTweet(int tweetId)
        {
            try
            {
                Response.StatusCode = 204;
                await TweetService.DeleteEnt(tweetId);
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
