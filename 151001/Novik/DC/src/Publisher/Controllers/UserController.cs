using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Services.interfaces;

namespace Publisher.Controllers;
[Route("api/v1.0/[controller]")]
[ApiController]
public class UsersController : ControllerBase
{
    private readonly IUserService _userService;

    public UsersController(IUserService userService)
    {
        _userService = userService;
    }
    // GET: api/v1.0/User
    [HttpGet]
    public async Task<ActionResult<IEnumerable<UserResponseTo>>> GetAllUsers()
    {
        var users = await _userService.GetAllAsync();
        /*if (!users.Any())
        {
            return NotFound(new { Error = "Entity not found" });
        }*/
        return Ok(users);
    }

    // GET: api/v1.0/User/5
    [HttpGet("{id}")]
    public async Task<ActionResult<UserResponseTo>> GetUserById([FromRoute]long id)
    {
        var user = await _userService.GetByIdAsync(id);
        return Ok(user);
        /*try
        {
            var user = await _userService.GetUserByIdAsync(id);
            return Ok(user);
        }
        catch (Exception ex)
        {
            return NotFound(new { Error = "Entity not found" });
            return Ok();
        }*/


    }

    // POST: api/v1.0/User
    [HttpPost]
    public async Task<ActionResult<UserResponseTo>> CreateUser([FromBody]UserRequestTo user)
    {
        if (!ModelState.IsValid)
        {
            return BadRequest(ModelState);
        }

        try
        {
            var userAdded = await _userService.AddAsync(user);
            return CreatedAtAction(nameof(GetUserById), new { id = userAdded.id }, userAdded);
        }

        catch (DbUpdateException ex)
        {
            return StatusCode(403,new { Error = "IDK" });
        }
        catch (Exception ex)
        {
            return BadRequest(new { Error = "IDK" });
        }
        
        
    }

    // PUT: api/v1.0/User/5
    [HttpPut]
    public async Task<ActionResult<UserResponseTo>> UpdateUser(UserRequestTo user)
    {
        if (!ModelState.IsValid)
        {
            return BadRequest(ModelState);
        }
        try
        {
            var userUpdate = await _userService.UpdateAsync(user);
            return Ok(userUpdate);
        }
        catch (Exception ex)
        {
            // Обработка ошибок, если обновление не удалось
            return NotFound(new { Error = "Entity not found" });
        }
        
    }

    // DELETE: api/v1.0/User/5
    [HttpDelete("{id}")]
    public async Task<ActionResult<UserResponseTo>> DeleteUser(int id)
    {
        try
        {
            await _userService.DeleteAsync(id);
            return NoContent();
        }
        catch (Exception e)
        {
            return NotFound(new { Error = "Entity not found" });
        }
        
    }
}