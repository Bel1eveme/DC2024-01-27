using Confluent.Kafka;
using Microsoft.EntityFrameworkCore;
using Publisher.Infrastructure.Redis.Implementations;
using Publisher.Infrastructure.Redis.Interfaces;
using Publisher.Mapper;
using Publisher.Models.Entity;
using Publisher.Repositories.Db;
using Publisher.Repositories.interfaces;
using Publisher.Services;
using Publisher.Services.interfaces;

namespace Publisher;
public class Program
{
    public static void Main(string[] args)
    {
        var builder = WebApplication.CreateBuilder(args);

        builder.Services.AddDbContext<AppDbContext>(options =>
            options.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection")));
 

        builder.Services.Configure<ProducerConfig>(builder.Configuration.GetRequiredSection("Kafka:Producer"));
        builder.Services.Configure<ConsumerConfig>(builder.Configuration.GetRequiredSection("Kafka:Consumer"));
        
        
        builder.Services.AddStackExchangeRedisCache(options =>
        {
            var connection = builder.Configuration.GetConnectionString("Redis");
            options.Configuration = connection;
        });
        builder.Services.AddSingleton<ICacheService, CacheService>();
// Add services to the container.
        builder.Services.AddControllersWithViews();
        builder.Services.AddAutoMapper(typeof(LabelMapper));
        builder.Services.AddAutoMapper(typeof(NewsMapper));

        builder.Services.AddAutoMapper(typeof(UserMapper));

        builder.Services.AddScoped<ILabelRepository, LabelRepository>();
        builder.Services.AddScoped<IUserRepository, UserRepository>();
        builder.Services.AddScoped<INewsRepository, NewsRepository>();


        builder.Services.AddTransient<IUserService,UserService>();
        builder.Services.AddTransient<ILabelService,LabelService>();
        builder.Services.AddTransient<INewsService,NewsService>();
        builder.Services.AddSingleton<IPostService, PostService>();

        builder.Services.AddHttpClient();
        var app = builder.Build();

// Configure the HTTP request pipeline.
        if (!app.Environment.IsDevelopment())
        {
            app.UseExceptionHandler("/Home/Error");
            // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
            app.UseHsts();
        }

        app.UseHttpsRedirection();
        app.UseStaticFiles();

        app.UseRouting();

        app.UseAuthorization();

        app.MapControllerRoute(
            name: "default",
            pattern: "{controller=Home}/{action=Index}/{id?}");

        app.Run();
    }
}


/*влад лох*/





































































































































































































































































































































































/*точно лох*/