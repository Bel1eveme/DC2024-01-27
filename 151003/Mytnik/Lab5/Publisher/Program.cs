using Confluent.Kafka;
using Publisher.Kafka;
using Publisher.Repository.Implementation;
using Publisher.Repository.Interface;
using Publisher.Service.Implementation;
using Publisher.Service.Interface;
using Publisher.Storage.Common;
using Publisher.Storage.SqlDb;
using MyCoolMapper = Publisher.Entity.Common.AutoMapper;

await KafkaConfig.CreateTopicIfNotExists();

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddControllers();
builder.Services.AddDbContext<DbStorage, PostgresDbContext>();
new PostgresDbContext().Database.EnsureDeleted();
builder.Services.AddControllers().AddNewtonsoftJson();
builder.Services.AddAutoMapper(typeof(MyCoolMapper));
builder.Services.AddStackExchangeRedisCache(options =>
{
    options.Configuration = "redis";
    options.InstanceName = "dc";
});
builder.Services
    .AddScoped<ICreatorService, CreatorService>()
    .AddScoped<IMarkerService, MarkerService>()
    .AddScoped<ITweetService, TweetService>()
    .AddScoped<INoteService, NoteService>()
    .AddScoped<ICreatorRepository, CreatorRepository>()
    .AddScoped<IMarkerRepository, MarkerRepository>()
    .AddScoped<ITweetRepository, TweetRepository>();
builder.Services
    .AddSingleton(provider =>
    {
        var producerConfig = new ProducerConfig
        {
            BootstrapServers = "kafka:9092"
        };

        return new ProducerBuilder<string, string>(producerConfig).Build();
    });

var app = builder.Build();
app.MapControllers();
app.Run();
