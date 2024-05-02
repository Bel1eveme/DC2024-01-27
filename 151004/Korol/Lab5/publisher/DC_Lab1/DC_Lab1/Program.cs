using Microsoft.EntityFrameworkCore;
using DC_Lab1;
using DC_Lab1.Services.Interfaces;
using DC_Lab1.Services;
using AutoMapper;
using DC_Lab1.Models;
using DC_Lab1.DTO;
using DC_Lab1.DB.BaseDBContext;
using DC_Lab1.DB;
using Confluent.Kafka;
using Microsoft.Extensions.DependencyInjection.Extensions;
using static Confluent.Kafka.ConfigPropertyNames;
using StackExchange.Redis;

var builder = WebApplication.CreateBuilder(args);
ConnectionMultiplexer redis = ConnectionMultiplexer.Connect("localhost");

// Add services to the container.
builder.Services.AddControllers();
builder.Services.AddDbContext<BaseContext, PostgreSQlDbContext>();
builder.Services
    .AddScoped<IAuthorService, AuthorService>()
    .AddScoped<IStoryService, StoryService>()
    .AddScoped<IMessageService, MessagesService>()
    .AddScoped<IStickerService, StickerService>();
builder.Services.AddAutoMapper(typeof(MappingProfile));


builder.Services.AddSingleton<IConsumer<Ignore, string>>(x =>
{
    var consumerConfig = new ConsumerConfig
    {
        BootstrapServers = "localhost:9092",
        GroupId = "my-group",
        AutoOffsetReset = AutoOffsetReset.Earliest
    };

    var consumer = new ConsumerBuilder<Ignore, string>(consumerConfig).Build();
    consumer.Subscribe("OutTopic");

    return consumer;
});

builder.Services.AddSingleton<IProducer<Null, string>>(x =>
{
    IProducer<Null, string> producer = new ProducerBuilder<Null, string>(new ProducerConfig { BootstrapServers = "localhost:9092" }).Build();

    return producer;
});

builder.Services.AddSingleton<IDatabase>(x =>
{
    IDatabase redisDB = redis.GetDatabase();

    return redisDB;
});

var app = builder.Build();

// Configure the HTTP request pipeline.
app.UseHttpsRedirection();
app.UseAuthorization();
app.MapControllers();
app.Run();


