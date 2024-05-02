using System.Collections.Concurrent;
using FluentValidation;
using Forum.Api;
using Forum.Api.Kafka;
using Forum.Api.Kafka.Consumer;
using Forum.Api.Kafka.Messages;
using Forum.Api.Models.Dto;
using Forum.Api.Repositories;
using Forum.Api.Services;
using Forum.Api.Validation;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddDbContext<AppDbContext>(
    options => options.UseNpgsql(builder.Configuration.GetConnectionString("DB_CONNECTION_STRING")));

/*builder.Services.AddDbContext<AppDbContext>(
    options => options.UseNpgsql(Environment.GetEnvironmentVariable("DB_CONNECTION_STRING")));*/

builder.Services.AddScoped<ICreatorRepository, CreatorRepository>();
builder.Services.AddScoped<ICreatorService, CreatorService>();


builder.Services.AddScoped<IPostRepository, PostRepository>();
builder.Services.AddScoped<IPostService, PostService>();

builder.Services.AddScoped<IStoryRepository, StoryRepository>();
builder.Services.AddScoped<IStoryService, StoryService>();

builder.Services.AddScoped<ITagRepository, TagRepository>();
builder.Services.AddScoped<ITagService, TagService>();

//builder.Services.Decorate<IPostRepository, CachedPostRepository>();
//builder.Services.Decorate<ITagRepository, CachedTagRepository>();
//builder.Services.Decorate<IStoryRepository, CachedStoryRepository>();
//builder.Services.Decorate<ICreatorRepository, CachedCreatorRepository>();




builder.Services.AddControllers();
builder.Services.AddProblemDetails();
builder.Services.AddAutoMapper(typeof(AppMappingProfile));
builder.Services.AddValidatorsFromAssemblyContaining<CreatorRequestDtoValidator>();

builder.Services.AddSingleton(typeof(MessageManager<,>));

builder.Services.AddKafkaMessageBus();
builder.Services.AddKafkaProducer<string, KafkaMessage>(config => {
    config.Topic = "in-topic";
    config.BootstrapServers = "localhost:9092";
});
builder.Services.AddSingleton(new ConcurrentDictionary<string, TaskCompletionSource<KafkaMessage>>());
builder.Services.AddKafkaConsumer<string, KafkaMessage, PostKafkaHandler>(p =>
{
    p.Topic = "out-topic";
    p.GroupId = "posts-group";
    p.BootstrapServers = "localhost:9092";
});

var app = builder.Build();

// Configure the HTTP request pipeline.

app.UseRouting();

app.UseExceptionHandler(new ExceptionHandlerOptions
{
    AllowStatusCode404Response = true,
    ExceptionHandlingPath = "/error"
});

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.MapGet("/test-error", () => {
    throw new Exception("This is a test exception.");
});

//app.UseHttpsRedirection();

app.MapControllers();


using (var scope = app.Services.CreateScope())
{
    var dbContext = scope.ServiceProvider.GetRequiredService<AppDbContext>();

    await dbContext.Database.EnsureCreatedAsync();
}


app.Run();
