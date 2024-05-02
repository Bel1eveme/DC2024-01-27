using AutoMapper;
using Microsoft.Extensions.Caching.Distributed;
using Newtonsoft.Json;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Repository.Interface;
using Publisher.Service.Implementation.Common;
using Publisher.Service.Interface;

namespace Publisher.Service.Implementation
{
    public class UserService(IMapper mapper, IUserRepository repository, IDistributedCache cache) :
        AbstractCrudService<User, UserRequestTO, UserResponseTO>(mapper, repository), IUserService
    {
        private readonly IMapper _mapper = mapper;

        public override async Task<bool> Remove(int id)
        {
            await cache.RemoveAsync(GetRedisId(id));

            return await base.Remove(id);
        }

        public override async Task<UserResponseTO> GetByID(int id)
        {
            var cacheResponse = await cache.GetStringAsync(GetRedisId(id));

            if (cacheResponse is null)
            {
                var response = await base.GetByID(id);
                await cache.SetStringAsync(GetRedisId(id), JsonConvert.SerializeObject(response));

                return response;
            }

            _ = Task.Run(async () =>
            {
                var res = await base.GetByID(id);
                await cache.SetStringAsync(GetRedisId(id), JsonConvert.SerializeObject(res));
            });

            return JsonConvert.DeserializeObject<UserResponseTO>(cacheResponse)
                ?? throw new Exception("Unable to deserialize User");
        }

        public override async Task<UserResponseTO> Add(UserRequestTO userTo)
        {
            if (!Validate(userTo))
            {
                throw new InvalidDataException("User is not valid");
            }

            var res = await base.Add(userTo);
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        public override async Task<UserResponseTO> Update(UserRequestTO userTo)
        {
            if (!Validate(userTo))
            {
                throw new InvalidDataException($"UPDATE invalid data: {userTo}");
            }

            var res = await base.Update(userTo);
            await cache.RemoveAsync(GetRedisId(res.Id));
            await cache.SetStringAsync(GetRedisId(res.Id), JsonConvert.SerializeObject(res));

            return res;
        }

        private static string GetRedisId(int id) => $"User:{id}";

        public async Task<UserResponseTO> GetByNewsID(int newsId)
        {
            var response = await repository.GetByNewsIdAsync(newsId);

            return _mapper.Map<UserResponseTO>(response.User);
        }

        private static bool Validate(UserRequestTO user)
        {
            var fnameLen = user.FirstName.Length;
            var lnameLen = user.LastName.Length;
            var passLen = user.Password.Length;
            var loginLen = user.Login.Length;

            if (fnameLen < 2 || fnameLen > 64)
            {
                return false;
            }
            if (lnameLen < 2 || fnameLen > 64)
            {
                return false;
            }
            if (passLen < 8 || passLen > 128)
            {
                return false;
            }
            if (loginLen < 2 || loginLen > 64)
            {
                return false;
            }
            return true;
        }
    }
}
