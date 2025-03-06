
namespace APIService.Models
{
    public class LessonModel
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public int UnitId { get; set; }
        public string? Description { get; set; }
        public string? Note { get; set; }
        public int Status { get; set; }
    }
}
