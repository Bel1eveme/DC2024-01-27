using Discussion.Middleware;
using Discussion.NoteEntity;
using Discussion.NoteEntity.Interface;
using Discussion.Storage.Cassandra;
using MyCoolMapper = Discussion.Common.AutoMapper;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddControllers().AddNewtonsoftJson();
builder.Services.AddSingleton<Random>();
builder.Services
    .AddScoped<CassandraStorage>()
    .AddScoped<INoteRepository, NoteRepository>()
    .AddScoped<INoteService, NoteService>();
builder.Services.AddAutoMapper(typeof(MyCoolMapper));

var app = builder.Build();
app.MapControllers();
app.UseURLLog();
app.Run();
