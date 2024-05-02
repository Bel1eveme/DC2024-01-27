using Confluent.Kafka;
using Discussion.Kafka;
using Discussion.Middleware;
using Discussion.NoteEntity;
using Discussion.NoteEntity.Interface;
using Discussion.Storage.Cassandra;
using MyCoolMapper = Discussion.Common.AutoMapper;

await KafkaConfig.CreateTopicIfNotExists();

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddControllers().AddNewtonsoftJson();
builder.Services.AddSingleton<Random>();
builder.Services.AddAutoMapper(typeof(MyCoolMapper));
builder.Services
    .AddScoped<CassandraStorage>()
    .AddScoped<INoteRepository, NoteRepository>()
    .AddScoped<INoteService, NoteService>();
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
app.UseURLLog();
using (var scope = app.Services.CreateScope())
{
    var postService = scope.ServiceProvider.GetRequiredService<INoteService>();
    var postProducer = scope.ServiceProvider.GetRequiredService<IProducer<string, string>>();

    new Thread(async () =>
    {
        var listener = new NoteKafkaController(postProducer, postService);
        await listener.StartConsuming();
    }).Start();
}

app.Run();
