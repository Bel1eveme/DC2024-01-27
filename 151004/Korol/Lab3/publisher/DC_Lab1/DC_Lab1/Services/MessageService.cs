using DC_Lab1.DTO.Interface;
using DC_Lab1.DTO;
using Microsoft.EntityFrameworkCore;
using DC_Lab1.Services.Interfaces;
using AutoMapper;
using DC_Lab1.Models;
using Microsoft.AspNetCore.Components.Forms;
using DC_Lab1.DB.BaseDBContext;
using Humanizer;
using System.Net.Http.Json;
using Newtonsoft.Json;
using System.Text;
using Microsoft.AspNetCore.Mvc;
using System.Net.Http;

namespace DC_Lab1.Services
{
    public class MessagesService(IMapper _mapper, BaseContext dbContext) : IMessageService
    {
        HttpClient client = new HttpClient();
        
        public async Task<IResponseTo> CreateEnt(IRequestTo Dto)
        {
            client.BaseAddress = new Uri("http://localhost:24130/");
            string json;
            if (Dto is MessageRequestTo messageRequest)
            {
                json = $"{{\"{messageRequest.Id}\":0,\"storyId\":{messageRequest.StoryId},\"content\":\"{messageRequest.Content}\"}}";
                var stringContent = new StringContent(json, Encoding.UTF8, "application/json");
                var response = await client.PostAsync("api/v1.0/messages", stringContent);

                var jsonDeSer = await response.Content.ReadAsStringAsync();
                var messageResponse = JsonConvert.DeserializeObject<MessageResponseTo>(jsonDeSer);

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
            var response = client.DeleteAsync($"api/v1.0/messages/{id}").Result;
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
            var response = await client.GetAsync($"api/v1.0/messages/{id}");

            var jsonDeSer = await response.Content.ReadAsStringAsync();
            var messageResponse = JsonConvert.DeserializeObject<MessageResponseTo>(jsonDeSer);

            var mappedResponse = _mapper.Map<MessageResponseTo>(messageResponse);

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
            var response = client.GetAsync("api/v1.0/messages").Result;

            var jsonDeSer = response.Content.ReadAsStringAsync().Result;
            var messageResponses = JsonConvert.DeserializeObject<List<MessageResponseTo>>(jsonDeSer);

            var mappedResponses = messageResponses.Select(_mapper.Map<MessageResponseTo>);
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
            if (Dto is MessageRequestTo messageRequest)
            {
                json = $"{{\"id\":{messageRequest.Id},\"storyId\":{messageRequest.StoryId},\"content\":\"{messageRequest.Content}\"}}";
                var stringContent = new StringContent(json, Encoding.UTF8, "application/json");
                var response = await client.PutAsync("api/v1.0/messages", stringContent);

                var jsonDeSer = await response.Content.ReadAsStringAsync();
                var messageResponse = JsonConvert.DeserializeObject<MessageResponseTo>(jsonDeSer);
                var mappedResponse = _mapper.Map<MessageResponseTo>(messageResponse);

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

        public bool Validate(MessageRequestTo MessageDto)
        {
            if (MessageDto?.Content?.Length < 2 || MessageDto?.Content?.Length > 2048)
                return false;

            return true;
        }
    }
}
