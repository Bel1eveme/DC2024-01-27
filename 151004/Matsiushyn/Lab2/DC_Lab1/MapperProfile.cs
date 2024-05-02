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
            CreateMap<TweetRequestTo, Tweet>();
            CreateMap<Tweet, TweetResponseTo>();
            CreateMap<PostRequestTo, Post>();
            CreateMap<Post, PostResponseTo>();
            CreateMap<Label, LabelResponseTo>();
            CreateMap<LabelRequestTo, Label>();
        }
    }
}
