using AutoMapper;
using DC_Lab1.DTO;
using DC_Lab1.Models;
using System;

namespace DC_Lab1
{
    public class MappingProfile : Profile
    {
        public MappingProfile()
        {
            CreateMap<MessageRequestTo, Message>();
            CreateMap<Message, MessageResponseTo>();
        }
    }
}
