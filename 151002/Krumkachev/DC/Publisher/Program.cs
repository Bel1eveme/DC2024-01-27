using Newtonsoft.Json;
using Publisher.Extensions;
using Publisher.Middleware;

var builder = WebApplication.CreateBuilder(args);

builder.Services
    .AddControllers()
    .AddNewtonsoftJson(options =>
        options.SerializerSettings.ReferenceLoopHandling = ReferenceLoopHandling.Ignore
    );
builder.Services.AddApiVersioning();
builder.Services
    .AddDbContext(builder.Configuration)
    .AddEndpointsApiExplorer()
    .AddSwaggerGen()
    .AddServices()
    .AddRepositories()
    .AddDiscussionClient()
    .AddInfrastructure();

var app = builder.Build();

app.UseGlobalErrorHandler();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseAuthorization();

app.MapControllers();

app.Run();