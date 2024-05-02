using AutoMapper;
using Publisher.Entity.Db;
using Publisher.Entity.DTO.RequestTO;
using Publisher.Entity.DTO.ResponseTO;
using Publisher.Repository.Interface;
using Publisher.Service.Implementation.Common;
using Publisher.Service.Interface;

namespace Publisher.Service.Implementation
{
    public class UserService(IMapper mapper, IUserRepository repository) :
        AbstractCrudService<User, UserRequestTO, UserResponseTO>(mapper, repository), IUserService
    {
        private readonly IMapper _mapper = mapper;

        public override async Task<UserResponseTO> Add(UserRequestTO userTo)
        {
            if (!Validate(userTo))
            {
                throw new InvalidDataException("User is not valid");
            }

            return await base.Add(userTo);
        }

        public override async Task<UserResponseTO> Update(UserRequestTO userTo)
        {
            if (!Validate(userTo))
            {
                throw new InvalidDataException($"UPDATE invalid data: {userTo}");
            }

            return await base.Update(userTo);
        }

        public async Task<UserResponseTO> GetByNewsID(int userId)
        {
            var response = await repository.GetByNewsIdAsync(userId);

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
