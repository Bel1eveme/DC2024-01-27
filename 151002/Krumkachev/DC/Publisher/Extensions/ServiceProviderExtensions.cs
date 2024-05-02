using FluentValidation;
using Microsoft.EntityFrameworkCore;
using Publisher.Data;
using Publisher.HttpClients.Implementations;
using Publisher.HttpClients.Interfaces;
using Publisher.Infrastructure.Mapper;
using Publisher.Infrastructure.Validators;
using Publisher.Repositories.Implementations;
using Publisher.Repositories.Interfaces;
using Publisher.Services.Implementations;
using Publisher.Services.Interfaces;

namespace Publisher.Extensions;

public static class ServiceProviderExtensions
{
	public static IServiceCollection AddDbContext(this IServiceCollection services, IConfigurationManager config)
	{
		services.AddDbContext<AppDbContext>(options =>
			options
				.UseLazyLoadingProxies()
				.UseNpgsql(config.GetConnectionString("PostgresConnection")));
		return services;
	}

	public static IServiceCollection AddRepositories(this IServiceCollection services)
	{
		services.AddScoped<ICreatorRepository, CreatorSqlRepository>();
		services.AddScoped<IIssueRepository, IssueSqlRepository>();
		services.AddScoped<ILabelRepository, LabelSqlRepository>();

		return services;
	}

	public static IServiceCollection AddServices(this IServiceCollection services)
	{
		services.AddScoped<ICreatorService, CreatorService>();
		services.AddScoped<IIssueService, IssueService>();
		services.AddScoped<IDiscussionClient, DiscussionClient>();
		services.AddScoped<ILabelService, LabelService>();

		return services;
	}

	public static IServiceCollection AddDiscussionClient(this IServiceCollection services)
	{
		services
			.AddHttpClient(nameof(DiscussionClient), client => client.BaseAddress = new Uri("http://localhost:24130/api/v1/"));

		return services;
	}

	public static IServiceCollection AddInfrastructure(this IServiceCollection services)
	{
		services.AddAutoMapper(typeof(MappingProfile));
		services.AddValidatorsFromAssemblyContaining<CreatorRequestDtoValidator>();

		return services;
	}
}