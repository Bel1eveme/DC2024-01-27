using Asp.Versioning;
using Confluent.Kafka;
using EntityFramework.Exceptions.PostgreSQL;
using FluentValidation;
using Microsoft.EntityFrameworkCore;
using REST.Publisher.Data;
using REST.Publisher.Infrastructure.Redis.Implementations;
using REST.Publisher.Infrastructure.Redis.Interfaces;
using REST.Publisher.Models.Entities;
using REST.Publisher.Models.Validators;
using REST.Publisher.Repositories.Implementations.EFCore;
using REST.Publisher.Repositories.Interfaces;
using REST.Publisher.Services.Implementations;
using REST.Publisher.Services.Interfaces;
using REST.Publisher.Utilities.Exceptions.Handler;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddExceptionHandler<GlobalExceptionHandler>();

builder.Services.Configure<ProducerConfig>(builder.Configuration.GetRequiredSection("Kafka:Producer"));
builder.Services.Configure<ConsumerConfig>(builder.Configuration.GetRequiredSection("Kafka:Consumer"));

builder.Services.AddStackExchangeRedisCache(options =>
{
    options.Configuration = builder.Configuration["Redis:Server"];
});
builder.Services.AddSingleton<ICacheService, CacheService>();

builder.Services.AddDbContext<AppDbContext>(opt =>
    opt.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection")).UseExceptionProcessor());

builder.Services.AddAutoMapper(AppDomain.CurrentDomain.GetAssemblies());

builder.Services.AddControllers();

builder.Services.AddApiVersioning(options =>
{
    options.AssumeDefaultVersionWhenUnspecified = true;
    options.DefaultApiVersion = new ApiVersion(1, 0);
    options.ReportApiVersions = true;
    options.ApiVersionReader = new UrlSegmentApiVersionReader();
}).AddApiExplorer(options =>
{
    options.GroupNameFormat = "'v'VVV";
    options.SubstituteApiVersionInUrl = true;
});

// Services Registration

builder.Services.AddTransient<IEditorService, EditorService>();
builder.Services.AddTransient<IIssueService, IssueService>();
builder.Services.AddTransient<ITagService, TagService>();
builder.Services.AddSingleton<INoteService, NoteService>();

// Validators Registration

builder.Services.AddTransient<AbstractValidator<Editor>, EditorValidator>();
builder.Services.AddTransient<AbstractValidator<Issue>, IssueValidator>();
builder.Services.AddTransient<AbstractValidator<Tag>, TagValidator>();

// Repository Registration

// In-memory
// builder.Services.AddSingleton<IEditorRepository<long>, EditorRepository>();
// builder.Services.AddSingleton<IIssueRepository<long>, IssueRepository>();
// builder.Services.AddSingleton<INoteRepository<long>, NoteRepository>();
// builder.Services.AddSingleton<ITagRepository<long>, TagRepository>();

// EF Core
builder.Services.AddScoped<IEditorRepository<long>, EditorRepository>();
builder.Services.AddScoped<IIssueRepository<long>, IssueRepository>();
builder.Services.AddScoped<ITagRepository<long>, TagRepository>();

var app = builder.Build();

app.UseExceptionHandler(_ => { });

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.MapControllers();

app.Run();

public partial class Program;