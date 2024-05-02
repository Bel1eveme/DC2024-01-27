using AutoMapper;
using Microsoft.AspNetCore.Mvc;
using Publisher.Controllers.V1_0.Common;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Service.Interface;
using System.Net;

namespace Publisher.Controllers.V1_0
{
    [Route("api/v1.0/users")]
    [ApiController]
    public class UserController(IUserService UserService, ILogger<UserController> Logger, IMapper Mapper) :
        AbstractController<User, UserRequestTO, UserResponseTO>(UserService, Logger, Mapper)
    {
        [HttpGet]
        [Route("news/{id:int}")]
        public async Task<JsonResult> GetByNewsID([FromRoute] int id)
        {
            UserResponseTO? response = null;
            Logger.LogInformation("Getting user by news ID: {id}", id);

            try
            {
                response = await UserService.GetByNewsID(id);
            }
            catch (Exception ex)
            {
                Logger.LogError("Error getting user by news ID {ex}", ex);
                Response.StatusCode = (int)HttpStatusCode.BadRequest;
            }

            return Json(response);
        }
    }
}
