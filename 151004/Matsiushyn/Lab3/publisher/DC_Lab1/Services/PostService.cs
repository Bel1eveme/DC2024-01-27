using DC_Lab1.DTO.Interface;
using DC_Lab1.DTO;
using Microsoft.EntityFrameworkCore;
using DC_Lab1.Services.Interfaces;
using AutoMapper;
using DC_Lab1.Models;
using Microsoft.AspNetCore.Components.Forms;
using DC_Lab1.DB.BaseDBContext;
using Newtonsoft.Json;
using System.Text;

namespace DC_Lab1.Services
{
    public class PostsService(IMapper _mapper, BaseContext dbContext) : IPostService
    {
        HttpClient client = new HttpClient();
        public async Task<IResponseTo> CreateEnt(IRequestTo Dto)
        {
            client.BaseAddress = new Uri("http://localhost:24130/");
            string json;
            if (Dto is PostRequestTo postRequest)
            {
                json = $"{{\"{postRequest.Id}\":0,\"tweetId\":{postRequest.tweetId},\"content\":\"{postRequest.Content}\"}}";
                var stringContent = new StringContent(json, Encoding.UTF8, "application/json");
                var response = await client.PostAsync("api/v1.0/posts", stringContent);

                var jsonDeSer = await response.Content.ReadAsStringAsync();
                var postResponse = JsonConvert.DeserializeObject<PostResponseTo>(jsonDeSer);

                if (response.IsSuccessStatusCode)
                {
                    return postResponse;
                }
                else
                {
                    throw new InvalidDataException("Incorrect data for CREATE Post");
                }
            }
            throw new InvalidDataException("Incorrect data for CREATE Post");

        }

        public async Task DeleteEnt(int id)
        {

            client.BaseAddress = new Uri("http://localhost:24130/");
            var response = client.DeleteAsync($"api/v1.0/posts/{id}").Result;
            if (response.IsSuccessStatusCode)
            {
                return;
            }
            else
            {
                throw new Exception("Deletting Post exception");
            }
        }

        public async Task<IResponseTo> GetEntById(int id)
        {
            client.BaseAddress = new Uri("http://localhost:24130/");
            var response = await client.GetAsync($"api/v1.0/posts/{id}");

            var jsonDeSer = await response.Content.ReadAsStringAsync();
            var postResponse = JsonConvert.DeserializeObject<PostResponseTo>(jsonDeSer);

            var mappedResponse = _mapper.Map<PostResponseTo>(postResponse);

            if (response.IsSuccessStatusCode)
            {
                return mappedResponse;
            }
            else
            {
                throw new ArgumentNullException($"Not found Post: {id}");
            }
        }

        public IEnumerable<IResponseTo> GetAllEnt()
        {
            client.BaseAddress = new Uri("http://localhost:24130/");
            var response = client.GetAsync("api/v1.0/posts").Result;

            var jsonDeSer = response.Content.ReadAsStringAsync().Result;
            var postResponses = JsonConvert.DeserializeObject<List<PostResponseTo>>(jsonDeSer);

            var mappedResponses = postResponses.Select(_mapper.Map<PostResponseTo>);
            try
            {
                return mappedResponses;

            }
            catch
            {
                throw new Exception("Getting all Posts exception");
            }
        }

        public async Task<IResponseTo> UpdateEnt(IRequestTo Dto)
        {
            client.BaseAddress = new Uri("http://localhost:24130/");
            string json;
            if (Dto is PostRequestTo postRequest)
            {
                json = $"{{\"id\":{postRequest.Id},\"tweetId\":{postRequest.tweetId},\"content\":\"{postRequest.Content}\"}}";
                var stringContent = new StringContent(json, Encoding.UTF8, "application/json");
                var response = await client.PutAsync("api/v1.0/posts", stringContent);

                var jsonDeSer = await response.Content.ReadAsStringAsync();
                var postResponse = JsonConvert.DeserializeObject<PostResponseTo>(jsonDeSer);
                var mappedResponse = _mapper.Map<PostResponseTo>(postResponse);

                if (response.IsSuccessStatusCode)
                {
                    return mappedResponse;
                }
                else
                {
                    throw new InvalidDataException("Incorrect data for UPDATE Post");
                }
            }
            throw new InvalidDataException("Incorrect data for UPDATE Post");

        }

        public bool Validate(PostRequestTo PostDto)
        {
            if (PostDto?.Content?.Length < 2 || PostDto?.Content?.Length > 2048)
                return false;

            return true;
        }
    }
}
