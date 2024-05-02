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
    public class PostService(IMapper _mapper, BaseContext dbContext) : IMessageService
    {
        HttpClient client = new HttpClient();
        public async Task<IResponseTo> CreateEnt(IRequestTo Dto)
        {
            client.BaseAddress = new Uri("http://localhost:24130/");
            string json;
            if (Dto is PostRequestTo messageRequest)
            {
                json = $"{{\"{messageRequest.Id}\":0,\"storyId\":{messageRequest.storyId},\"content\":\"{messageRequest.Content}\"}}";
                var stringContent = new StringContent(json, Encoding.UTF8, "application/json");
                var response = await client.PostAsync("api/v1.0/posts", stringContent);

                var jsonDeSer = await response.Content.ReadAsStringAsync();
                var messageResponse = JsonConvert.DeserializeObject<PostResponseTo>(jsonDeSer);

                if (response.IsSuccessStatusCode)
                {
                    return messageResponse;
                }
                else
                {
                    throw new InvalidDataException("Incorrect data for CREATE Message");
                }
            }
            throw new InvalidDataException("Incorrect data for CREATE Message");





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
                throw new Exception("Deletting Message exception");
            }

        }

        public async Task<IResponseTo> GetEntById(int id)
        {
            client.BaseAddress = new Uri("http://localhost:24130/");
            var response = await client.GetAsync($"api/v1.0/posts/{id}");

            var jsonDeSer = await response.Content.ReadAsStringAsync();
            var messageResponse = JsonConvert.DeserializeObject<PostResponseTo>(jsonDeSer);

            var mappedResponse = _mapper.Map<PostResponseTo>(messageResponse);

            if (response.IsSuccessStatusCode)
            {
                return mappedResponse;
            }
            else
            {
                throw new ArgumentNullException($"Not found Message: {id}");
            }


        }

        public IEnumerable<IResponseTo> GetAllEnt()
        {
            client.BaseAddress = new Uri("http://localhost:24130/");
            var response = client.GetAsync("api/v1.0/posts").Result;

            var jsonDeSer = response.Content.ReadAsStringAsync().Result;
            var messageResponses = JsonConvert.DeserializeObject<List<PostResponseTo>>(jsonDeSer);

            var mappedResponses = messageResponses.Select(_mapper.Map<PostResponseTo>);
            try
            {
                return mappedResponses;

            }
            catch
            {
                throw new Exception("Getting all Messages exception");
            }
        }

        public async Task<IResponseTo> UpdateEnt(IRequestTo Dto)
        {
            client.BaseAddress = new Uri("http://localhost:24130/");
            string json;
            if (Dto is PostRequestTo messageRequest)
            {
                json = $"{{\"id\":{messageRequest.Id},\"storyId\":{messageRequest.storyId},\"content\":\"{messageRequest.Content}\"}}";
                var stringContent = new StringContent(json, Encoding.UTF8, "application/json");
                var response = await client.PutAsync("api/v1.0/posts", stringContent);

                var jsonDeSer = await response.Content.ReadAsStringAsync();
                var messageResponse = JsonConvert.DeserializeObject<PostResponseTo>(jsonDeSer);
                var mappedResponse = _mapper.Map<PostResponseTo>(messageResponse);

                if (response.IsSuccessStatusCode)
                {
                    return mappedResponse;
                }
                else
                {
                    throw new InvalidDataException("Incorrect data for UPDATE Message");
                }
            }
            throw new InvalidDataException("Incorrect data for UPDATE Message");


        }

        public bool Validate(PostRequestTo PostDto)
        {
            if (PostDto?.Content?.Length < 2 || PostDto?.Content?.Length > 2048)
                return false;

            return true;
        }
    }
}
