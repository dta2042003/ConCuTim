using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace APIService.Entity
{
    public class Setting
    {
        public int Id { get; set; }
        public string Name { get; set; } = null!;
        public int Status { get; set; }
        public string? Note { get; set; }
        public string Value { get; set; } = null!;
        public virtual ICollection<Setting> Settings { get; set; }
        public virtual Setting ParentSetting { get; set; }
        public int? ParentId { get; set; }
    }
}
