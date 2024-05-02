using System.Data.Common;
using Cassandra;
using DotNet.Testcontainers.Builders;
using DotNet.Testcontainers.Containers;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc.Testing;
using Microsoft.AspNetCore.TestHost;
using Microsoft.Extensions.DependencyInjection;
using REST.Discussion.Data;

namespace REST.Discussion.IntegrationTests.Fixtures;

public class RestWebApplicationFactory : WebApplicationFactory<Program>, IAsyncLifetime
{
    private const string BasePath = "api/v1.0/";

    private readonly IContainer _dbContainer = new ContainerBuilder()
        .WithImage("cassandra:latest")
        .WithPortBinding(9042, true)
        .WithWaitStrategy(Wait.ForUnixContainer().UntilPortIsAvailable(9042))
        .WithCleanUp(true)
        .Build();

    public HttpClient HttpClient { get; private set; } = null!;

    private IServiceScope _scope = null!;

    protected override void ConfigureWebHost(IWebHostBuilder builder)
    {
        builder.ConfigureTestServices(services =>
        {
            var descriptor = services.SingleOrDefault(d =>
                d.ServiceType == typeof(CassandraContext));

            if (descriptor != null) services.Remove(descriptor);

            services.AddSingleton<CassandraContext>(_ =>
                new CassandraContext(_dbContainer.Hostname,
                    "distcomp", _dbContainer.GetMappedPublicPort(9042)));
        });
    }

    public async Task InitializeAsync()
    {
        await _dbContainer.StartAsync();
        
        
        HttpClient = CreateClient();
        if (HttpClient.BaseAddress != null)
            HttpClient.BaseAddress = new Uri(HttpClient.BaseAddress.AbsoluteUri + BasePath);

        _scope = Services.CreateScope();

        await InitializeDbAsync();
    }

    private async Task InitializeDbAsync()
    {
        var cluster = Cluster.Builder()
            .AddContactPoint(_dbContainer.Hostname)
            .WithPort(_dbContainer.GetMappedPublicPort(9042))
            .Build();
        var session = await cluster.ConnectAsync();
  
        IStatement statement = new SimpleStatement("""
                                                                create table distcomp.tblnote
                                                                (
                                                                    country text,
                                                                    issueid bigint,
                                                                    id      bigint,
                                                                    content text,
                                                                    primary key (country, issueid, id)
                                                                );
                                                   """);
        await session.ExecuteAsync(statement);
    }

    public async Task ResetDatabase()
    {
        var context = _scope.ServiceProvider.GetRequiredService<CassandraContext>();
        
        IStatement statement = new SimpleStatement("TRUNCATE tblnote");
        await context.Session.ExecuteAsync(statement);
    }

    public new async Task DisposeAsync() => await _dbContainer.DisposeAsync();
}