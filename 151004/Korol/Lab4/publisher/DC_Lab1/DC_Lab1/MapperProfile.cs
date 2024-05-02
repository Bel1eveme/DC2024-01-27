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
            CreateMap<AuthorRequestTo, Author>();
            CreateMap<Author, AuthorResponseTo>();

            CreateMap<StoryRequestTo, Story>();
            CreateMap<Story, StoryResponseTo>();

            CreateMap<MessageRequestTo, Message>();
            CreateMap<Message, MessageResponseTo>();

            CreateMap<Sticker, StickerResponseTo>();
            CreateMap<StickerRequestTo, Sticker>();
        }
    }
}
