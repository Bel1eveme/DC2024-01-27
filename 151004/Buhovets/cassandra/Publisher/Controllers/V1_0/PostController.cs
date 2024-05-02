using Microsoft.AspNetCore.Mvc;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;

namespace Publisher.Controllers.V1_0
{
    [Route("api/v1.0/posts")]
    [ApiController]
    public class PostController : Controller
    {
        [HttpGet]
        [Route("{id:int}")]
        public async Task<JsonResult> GetByID([FromRoute] int id)
        {
            using var client = new HttpClient();
            var response = await client.GetAsync($"http://discussion:24130/api/v1.0/posts/{id}");
            var jsonResponse = await response.Content.ReadFromJsonAsync<PostResponseTO>();

            return new JsonResult(jsonResponse)
            {
                StatusCode = (int)response.StatusCode
            };
        }

        [HttpGet]
        public async Task<IActionResult> Read()
        {
            using var client = new HttpClient();
            var response = await client.GetAsync($"http://discussion:24130/api/v1.0/posts");
            var jsonResponse = await response.Content.ReadFromJsonAsync<ICollection<PostResponseTO>>();

            return new JsonResult(jsonResponse)
            {
                StatusCode = (int)response.StatusCode
            };
        }

        [HttpPost]
        public async Task<JsonResult> Create([FromBody] PostRequestTO request)
        {
            using var client = new HttpClient();
            var response = await client.PostAsJsonAsync("http://discussion:24130/api/v1.0/posts", request);
            var jsonResponse = await response.Content.ReadFromJsonAsync<PostResponseTO>();

            return new JsonResult(jsonResponse)
            {
                StatusCode = (int)response.StatusCode
            };
        }

        [HttpPut]
        public async Task<JsonResult> Update([FromBody] PostRequestTO request)
        {
            using var client = new HttpClient();
            var response = await client.PutAsJsonAsync("http://discussion:24130/api/v1.0/posts", request);
            var jsonResponse = await response.Content.ReadFromJsonAsync<PostResponseTO>();

            return new JsonResult(jsonResponse)
            {
                StatusCode = (int)response.StatusCode
            };
        }

        [HttpDelete]
        [Route("{id:int}")]
        public async Task<IActionResult> Delete([FromRoute] int id)
        {
            using var client = new HttpClient();
            var response = await client.DeleteAsync($"http://discussion:24130/api/v1.0/posts/{id}");

            return StatusCode((int)response.StatusCode);
        }
    }
}
