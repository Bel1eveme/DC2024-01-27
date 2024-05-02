using Asp.Versioning;
using LR1.ExceptionHandlers;
using LR1.Services.Interfaces;
using LR1.Storage;
using Microsoft.EntityFrameworkCore;
using LR1.Services.Implementations;

var builder = WebApplication.CreateBuilder(args);
builder.Services.AddControllers().AddJsonOptions(options => { options.JsonSerializerOptions.IncludeFields = true; });

// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddApiVersioning(config =>
{
    config.DefaultApiVersion = new ApiVersion(1, 0);
    config.AssumeDefaultVersionWhenUnspecified = true;
});

builder.Services.AddSwaggerGen();
builder.Services.AddDbContext<AppDbContext>(o => o.UseInMemoryDatabase("AppDb"));
builder.Services.AddScoped<ICreatorService, CreatorService>();
builder.Services.AddScoped<ICommentService, CommentService>();
builder.Services.AddScoped<ILabelService, LabelService>();
builder.Services.AddScoped<IIssueService, IssueService>();
builder.Services.AddExceptionHandler<GlobalExceptionHandler>();
builder.Services.AddProblemDetails();

var app = builder.Build();
app.UseExceptionHandler();
app.MapControllers();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();
app.UseAuthorization();
app.Run();