using Publisher.Middleware;
using Publisher.Repository.Implementation;
using Publisher.Repository.Interface;
using Publisher.Service.Implementation;
using Publisher.Service.Interface;
using Publisher.Storage.Common;
using Publisher.Storage.SqlDb;
using MyCoolMapper = Publisher.Entity.Common.AutoMapper;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddControllers();
builder.Services.AddDbContext<DbStorage, PostgresDbContext>();
new PostgresDbContext().Database.EnsureDeleted();
builder.Services.AddControllers().AddNewtonsoftJson();
builder.Services
    .AddScoped<IUserService, UserService>()
    .AddScoped<ILabelService, LabelService>()
    .AddScoped<INoteService, NoteService>()
    .AddScoped<INewsService, NewsService>()
    .AddScoped<IUserRepository, UserRepository>()
    .AddScoped<ILabelRepository, LabelRepository>()
    .AddScoped<INoteRepository, NoteRepository>()
    .AddScoped<INewsRepository, NewsRepository>();
builder.Services.AddAutoMapper(typeof(MyCoolMapper));

var app = builder.Build();
app.UseURLLog();
app.MapControllers();
app.Run();
