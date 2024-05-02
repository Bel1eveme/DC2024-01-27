using Cassandra;
using Cassandra.Mapping;
using Confluent.Kafka;
using Discussion.Infrastructure.Kafka;
using Discussion.Mapper;
using Discussion.Models.DTOs.Configurations;
using Discussion.Repositories.implementations;
using Discussion.Repositories.interfaces;
using Discussion.Services;
using Discussion.Services.interfaces;

namespace Discussion;

public class Program
{
    public static void Main(string[] args)
    {
        var builder = WebApplication.CreateBuilder(args);
        builder.Services.Configure<ProducerConfig>(builder.Configuration.GetRequiredSection("Kafka:Producer"));
        builder.Services.Configure<ConsumerConfig>(builder.Configuration.GetRequiredSection("Kafka:Consumer"));
        builder.Services.AddSingleton<MessageProcessor>();
        builder.Services.AddHostedService<ConsumerService>();
        var mappingConfig = new MappingConfiguration().Define(new CassandraMappings());

// Создание кластера Cassandra с использованием объекта конфигурации маппинга
        var cluster = Cluster.Builder()
            .AddContactPoint("127.0.0.1") // Адрес узла Cassandra
            .WithPort(9042) // Порт Cassandra
            .Build();

// Создание сессии Cassandra
        var session = cluster.Connect("distcomp"); // Название вашего keyspace

// Создание объекта маппера с использованием сессии и конфигурации маппинга
        var mapper = new Cassandra.Mapping.Mapper(session, mappingConfig);

        


// Add services to the container.
        builder.Services.AddControllersWithViews();
        builder.Services.AddAutoMapper(typeof(PostMapper));

        builder.Services.AddTransient<IPostRepository, PostRepository>();

        builder.Services.AddTransient<IPostService,PostService>();

        builder.Services.AddSingleton<IMapper>(mapper);



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