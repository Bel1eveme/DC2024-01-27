using System.Text;
using System.Text.Json;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.HttpClients.Interfaces;

namespace Publisher.HttpClients.Implementations;

public class DiscussionClient(IHttpClientFactory factory) : IDiscussionClient
{
	private static readonly JsonSerializerOptions JsonSerializerOptions = new()
	{
		PropertyNamingPolicy = JsonNamingPolicy.CamelCase
	};

	public async Task<IEnumerable<PostResponseDto>> GetNotesAsync()
	{
		var httpClient = factory.CreateClient(nameof(DiscussionClient));
		var response = await httpClient.GetAsync(new Uri("notes", UriKind.Relative));
		response.EnsureSuccessStatusCode();

		var responseJson = await response.Content.ReadAsStringAsync();
		return JsonSerializer.Deserialize<IEnumerable<PostResponseDto>>(responseJson, new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
	}

	public async Task<PostResponseDto> GetNoteByIdAsync(long id)
	{
		var httpClient = factory.CreateClient(nameof(DiscussionClient));
		var response = await httpClient.GetAsync(new Uri($"notes/{id}", UriKind.Relative));
		response.EnsureSuccessStatusCode();

		var responseJson = await response.Content.ReadAsStringAsync();
		return JsonSerializer.Deserialize<PostResponseDto>(responseJson, new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
	}

	public async Task<PostResponseDto> CreateNoteAsync(PostRequestDto post)
	{
		var httpClient = factory.CreateClient(nameof(DiscussionClient));

		var postJson = JsonSerializer.Serialize(post);
		var content = new StringContent(postJson, Encoding.UTF8, "application/json");

		var response = await httpClient.PostAsync("notes", content);
		response.EnsureSuccessStatusCode();

		var responseJson = await response.Content.ReadAsStringAsync();
		return JsonSerializer.Deserialize<PostResponseDto>(responseJson, new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
	}

	public async Task<PostResponseDto> UpdateNoteAsync(PostRequestDto post)
	{
		var httpClient = factory.CreateClient(nameof(DiscussionClient));
		var postJson = JsonSerializer.Serialize(post);
		var content = new StringContent(postJson, Encoding.UTF8, "application/json");

		var response = await httpClient.PutAsync("notes", content);
		response.EnsureSuccessStatusCode();

		var responseJson = await response.Content.ReadAsStringAsync();
		return JsonSerializer.Deserialize<PostResponseDto>(responseJson, new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
	}

	public async Task DeleteNoteAsync(long id)
	{
		var httpClient = factory.CreateClient(nameof(DiscussionClient));
		var response = await httpClient.DeleteAsync($"notes/{id}");
		response.EnsureSuccessStatusCode();
	}
}