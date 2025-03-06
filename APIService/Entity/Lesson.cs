using System;
using System.Collections.Generic;

namespace APIService.Entity
{
    public class Lesson
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public Unit Unit { get; set; }
        public int UnitId { get; set; }
        public string? Description { get; set; }
        public string? Note { get; set; }
        public int Status { get; set; }
    }
}
