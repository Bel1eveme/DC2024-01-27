using Discussion;
using Discussion.Repositories.implementations;
using Discussion.Repositories.interfaces;
using Discussion.Services;
using Discussion.Services.interfaces;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Mvc.Testing;
using Microsoft.AspNetCore.TestHost;
using Microsoft.Extensions.DependencyInjection;
using Moq;

namespace Tests.Discussion.Factory;

public class CustomWebApplicationFactory : WebApplicationFactory<Program>
{
    public Mock<IPostRepository> postRepositoryMock { get; }
    public Mock<IPostService> postServiceMock { get; }

    public CustomWebApplicationFactory()
    {
        postRepositoryMock = new Mock<IPostRepository>();
        postServiceMock = new Mock<IPostService>();
    }

    protected override void ConfigureWebHost(IWebHostBuilder builder)
    {
        base.ConfigureWebHost(builder);
        builder.ConfigureTestServices(services =>
        {
            // Remove any existing registrations for IPostRepository and IPostService
            var descriptorRepo = services.SingleOrDefault(
                d => d.ServiceType ==
                     typeof(IPostRepository));
            if (descriptorRepo != null)
            {
                services.Remove(descriptorRepo);
            }

            var descriptorService = services.SingleOrDefault(
                d => d.ServiceType ==
                     typeof(IPostService));
            if (descriptorService != null)
            {
                services.Remove(descriptorService);
            }

            // Add the actual implementations of IPostRepository and IPostService
            services.AddScoped<IPostRepository, PostRepository>();
            services.AddScoped<IPostService, PostService>();
        });
    }
}