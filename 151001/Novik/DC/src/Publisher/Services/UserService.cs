using AutoMapper;
using Publisher.Infrastructure.Redis.Interfaces;
using Publisher.Models.DTOs.Requests;
using Publisher.Models.DTOs.Responses;
using Publisher.Models.Entity;
using Publisher.Repositories.interfaces;
using Publisher.Services.interfaces;

namespace Publisher.Services;

public class UserService : IUserService
{
     private readonly IUserRepository _userRepository;
    private readonly IMapper _userMapper;
    private readonly ICacheService _cacheService;
    
    private const string Prefix = "user-";
    public UserService(IUserRepository userRepository,IMapper userMapper, ICacheService cacheService)
    {
        _userMapper = userMapper;
        _userRepository = userRepository;
        _cacheService = cacheService;
    }
    public async Task<IEnumerable<UserResponseTo>> GetAllAsync()
    {
        var users = (await _userRepository.GetAllAsync()).ToList();
        var result = new List<UserResponseTo>(users.Count);

        foreach (var user in users)
        {
            await _cacheService.SetAsync(Prefix + user.id, user);
            result.Add(_userMapper.Map<UserResponseTo>(user));
        }

        return result;
    }

    public async Task<UserResponseTo?>? GetByIdAsync(long id)
    {
        var foundEditor = await _cacheService.GetAsync(Prefix + id,
            async () => await _userRepository.GetByIdAsync(id));

        return _userMapper.Map<UserResponseTo>(foundEditor);
    }

    public async Task<UserResponseTo> AddAsync(UserRequestTo userRequest)
    {
        var userEntity = _userMapper.Map<User>(userRequest);
        userEntity = await _userRepository.AddAsync(userEntity);
        await _cacheService.SetAsync(Prefix + userEntity.id, userEntity);
        return _userMapper.Map<UserResponseTo>(userEntity);
    }

    public async Task<UserResponseTo> UpdateAsync(UserRequestTo userRequest)
    {
        if (userRequest == null) throw new ArgumentNullException(nameof(userRequest));
        var user = _userMapper.Map<User>(userRequest);


        var updatedEditor = await _userRepository.UpdateAsync(user);

        await _cacheService.SetAsync(Prefix + updatedEditor.id, updatedEditor);

        return _userMapper.Map<UserResponseTo>(updatedEditor);
    }

    public async Task DeleteAsync(long id)
    {
        await _cacheService.RemoveAsync(Prefix + id);
        await _userRepository.DeleteAsync(id);
    }
}