using APIService.Entity;

namespace APIService.Service.Interface.Exercises
{
    public interface IExercise
    {
        Task<List<Exercise>> GetAllExercise();
        Task<Exercise> GetExerciseById(int id);
        Task<int> AddExercise(Exercise lesson);
        Task<bool> UpdateExercise(Exercise lesson);
        Task<bool> DeleteExercise(int id);
        Task<List<Exercise>> getAllExercisesByLessonId(int? lessonId);
        Task<bool> ActiveExercise(int id);
    }
}
