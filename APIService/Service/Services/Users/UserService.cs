using Microsoft.EntityFrameworkCore;
using APIService.DataAccess;
using APIService.Entity;
using APIService.Service.Interface.Users;

namespace APIService.Service.Service.Users
{
    public class UserService : IUserService
    {
        private readonly TriolingoDbContext _context;
        public UserService(TriolingoDbContext context)
        {
            _context = context;
        }
        public async Task<User> Login(User user)
        {
            var userLoged = await _context.Users.Where(x => x.Email == user.Email && x.Password == user.Password).FirstOrDefaultAsync();
            return userLoged;
        }
        public async Task<User> FindExistEmail(string email)
        {
            var userLoged = await _context.Users.Where(x => x.Email == email).FirstOrDefaultAsync();
            return userLoged;
        }
        public async Task<bool> Regis(User user)
        {
            var check = FindExistEmail(user.Email);
            if (check == null)
            {
                User newUser = new User()
                {
                    Email = user.Email,
                    Password = user.Password,
                    FullName = user.FullName,
                };
                await _context.AddAsync(newUser);
                await _context.SaveChangesAsync();
                return true;
            }
            else
            {
                return false;
            }
        }
    }
}
