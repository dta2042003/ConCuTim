using System;
using System.Collections.Generic;

namespace APIService.Entity
{
    public class UserRole
    {
        public int Id { get; set; }
        public string? Note { get; set; }
        public User User { get; set; }
        public int UserId { get; set; }
        public Setting Setting { get; set; }
        public int RoleType { get; set; }
    }
}
