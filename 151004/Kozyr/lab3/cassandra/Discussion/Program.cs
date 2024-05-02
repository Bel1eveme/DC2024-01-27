using Discussion.Middleware;
using Discussion.CommentEntity;
using Discussion.CommentEntity.Interface;
using Discussion.Storage.Cassandra;
using MyCoolMapper = Discussion.Common.AutoMapper;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddControllers().AddNewtonsoftJson();
builder.Services.AddSingleton<Random>();
builder.Services
    .AddScoped<CassandraStorage>()
    .AddScoped<ICommentRepository, CommentRepository>()
    .AddScoped<ICommentService, CommentService>();
builder.Services.AddAutoMapper(typeof(MyCoolMapper));

var app = builder.Build();
app.MapControllers();
app.UseURLLog();
app.Run();
