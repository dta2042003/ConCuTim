using APIService.Entity;

namespace APIService.Service.Interface.Users
{
    public interface IUserService
    {
        Task<User> Login(User user);
        Task<bool> Regis(User user);
    }
}
