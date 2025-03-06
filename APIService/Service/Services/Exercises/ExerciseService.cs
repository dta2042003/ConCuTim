using Microsoft.EntityFrameworkCore;
using APIService.Service.Interface.Exercises;
using APIService.Entity;
using APIService.DataAccess;

namespace APIService.Service.Service.Exercises
{
    public class ExerciseService : IExercise
    {
        private readonly TriolingoDbContext _dbContext;
        public ExerciseService(TriolingoDbContext dbContext)
        {
            _dbContext = dbContext;
        }
        //public async Task<User> FindExistExercise(string name)
        //{
        //    var userLoged = await _dbContext.Exercises.Where(x => x.Name == name && x.Status == 1).FirstOrDefaultAsync();
        //    return userLoged;
        //}

        public async Task<List<Exercise>> GetAllExercise()
        {
            var exercises = await _dbContext.Exercises.Where(x => x.Status == 1).ToListAsync();
            //var result = _mapper.Map<List<Exercise>>(exercises);
            return exercises;

        }
        public async Task<Exercise> GetExerciseById(int id)
        {
            var exercises = await _dbContext.Exercises.Where(x => x.Id == id).FirstOrDefaultAsync();
            return exercises;
        }

        public async Task<int> AddExercise(Exercise exercise)
        {
                Exercise newUser = exercise;
                await _dbContext.AddAsync(newUser);
                await _dbContext.SaveChangesAsync();

                return newUser.Id;
        }

        public async Task<bool> UpdateExercise(Exercise exercise)
        {

            var exercises = await _dbContext.Exercises.Where(x => x.Id == exercise.Id).FirstOrDefaultAsync();
            if (exercises != null)
            {
                exercises.File = exercise.File;
                exercises.FileName = exercise.FileName;
                exercises.Title = exercise.Title;
                exercises.Description = exercise.Description;
                exercises.LessonId = exercise.LessonId;
                _dbContext.Update(exercises);
                await _dbContext.SaveChangesAsync();
                return true;
            }
            return false;

        }

        public async Task<bool> DeleteExercise(int id)
        {
            var exercises = await _dbContext.Exercises.Where(x =>x.Id == id).FirstOrDefaultAsync();
            if (exercises != null)
            {
                exercises.Status = 0;
                List<Question> questions = await _dbContext.Questions.Where(x => x.ExerciseId== id).ToListAsync();
                foreach (Question question in questions)
                {
                    question.Status = question.Status-1;
                    List<Answer> answers = await _dbContext.Answers.Where(x=>x.QuestionId== question.Id).ToListAsync();
                    foreach(Answer ans in answers)
                    {
                        ans.Status = ans.Status-1;
                    }
                    _dbContext.Answers.UpdateRange(answers);
                }
                _dbContext.Questions.UpdateRange(questions);
                _dbContext.Exercises.Update(exercises);
                await _dbContext.SaveChangesAsync();
            }
            return true;
        }

        public async Task<List<Exercise>> getAllExercisesByLessonId(int? lessonId)
        {
            var lessons = await _dbContext.Exercises.Where(x => x.LessonId == lessonId).ToListAsync();
            return lessons;
        }

        public async Task<bool> ActiveExercise(int id)
        {
            var exercises = await _dbContext.Exercises.Where(x => x.Id == id).FirstOrDefaultAsync();
            if (exercises != null)
            {
                exercises.Status = 1;
                List<Question> questions = await _dbContext.Questions.Where(x => x.ExerciseId == id).ToListAsync();
                foreach (Question question in questions)
                {
                    question.Status = question.Status + 1;
                    List<Answer> answers = await _dbContext.Answers.Where(x => x.QuestionId == question.Id).ToListAsync();
                    foreach (Answer ans in answers)
                    {
                        ans.Status = ans.Status + 1;
                    }
                    _dbContext.Answers.UpdateRange(answers);
                }
                _dbContext.Questions.UpdateRange(questions);
                _dbContext.Exercises.Update(exercises);
                await _dbContext.SaveChangesAsync();
            }
            return true;
        }
    }
}
