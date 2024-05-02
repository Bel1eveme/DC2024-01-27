using Microsoft.EntityFrameworkCore;
using DC_Lab1;
using DC_Lab1.Services.Interfaces;
using DC_Lab1.Services;
using AutoMapper;
using DC_Lab1.Models;
using DC_Lab1.DTO;
using DC_Lab1.DB.BaseDBContext;
using DC_Lab1.DB;
//
var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();

builder.Services.AddDbContext<BaseContext, PostgreSQlDbContext>();
builder.Services
    .AddScoped<IAuthorService, AuthorService>()
    .AddScoped<ITweetService, TweetService>()
    .AddScoped<IPostService, PostsService>()
    .AddScoped<ILabelService, LabelService>();
builder.Services.AddAutoMapper(typeof(MappingProfile));
var app = builder.Build();

// Configure the HTTP request pipeline.

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
