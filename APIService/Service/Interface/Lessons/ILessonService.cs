using APIService.Entity;

namespace APIService.Service.Interface.Lessons
{
    public interface ILessonService
    {
        Task<List<Lesson>> GetAllLesson();
        Task<Lesson> GetLessonById(int id);
        Task<bool> AddLesson(Lesson lesson);
        Task<bool> UpdateLesson(Lesson lesson);
        Task<bool> DeleteLesson(int id);
        Task<List<Lesson>> getAllLessonsByUnitId(int? unitId);
    }
}
