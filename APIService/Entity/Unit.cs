using System;
using System.Collections.Generic;

namespace APIService.Entity
{
    public class Unit
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string? Description { get; set; }
        public int Order { get; set; }
        public Course Course { get; set; }
        public int CourseId { get; set; }
        public int Status { get; set; }
        public string? Note { get; set; }
    }
}
