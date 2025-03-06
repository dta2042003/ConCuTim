using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace APIService.Entity
{
    public class Exercise
    {
        public int Id { get; set; }
        public int Status { get; set; }
        public string Title { get; set; }
        public string? Description { get; set; }
        public Setting Setting { get; set; }
        public int TypeId { get; set; }
        public virtual Lesson Lesson { get; set; }
        public int LessonId { get; set; }
        public string? File { get; set; }
        public string? FileName { get; set; }
        [NotMapped]
        public string? FileExtention { get; set; }
        [NotMapped]
        public string? FileUrl { get; set; }
        [NotMapped]
        public List<Question> Questions { get; set; }
    }
}
