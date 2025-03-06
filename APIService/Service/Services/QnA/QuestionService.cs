using Microsoft.EntityFrameworkCore;
using APIService.DataAccess;
using APIService.Entity;
using APIService.Service.Interface.QnA;

namespace APIService.Service.Service.QnA
{
    public class QuestionService : IQuestion
    {
        private readonly TriolingoDbContext _dbContext;
        public QuestionService(TriolingoDbContext dbContext)
        {
            _dbContext = dbContext;
        }

        public async Task<bool> ActiveQuestion(int Question)
        {
            var question = await _dbContext.Questions.Where(x => x.Id == Question).FirstOrDefaultAsync();
            if (question != null)
            {
                List<Answer> answerList = await _dbContext.Answers.Where(x => x.QuestionId == Question).ToListAsync();
                foreach (var answer in answerList)
                {
                    answer.Status = answer.Status + 1;
                }
                Exercise exercise = await _dbContext.Exercises.Where(x => x.Id == question.ExerciseId).FirstOrDefaultAsync();
                if (exercise != null && exercise.Status != 1)
                {
                    exercise.Status = 1;
                    _dbContext.Exercises.Update(exercise);
                }
                question.Status = 1;
                _dbContext.Answers.UpdateRange(answerList);
                _dbContext.Questions.Update(question);
                await _dbContext.SaveChangesAsync();
                return true;
            }
            return false;
        }

        public async Task<int> AddNewQuestion(Question Question)
        {
            await _dbContext.Questions.AddAsync(Question);
            await _dbContext.SaveChangesAsync();
            return Question.Id;
        }

        public async Task<bool> DeleteQuestion(int Question)
        {
            var question = await _dbContext.Questions.Where(x => x.Id == Question).FirstOrDefaultAsync();
            if (question != null)
            {
                List<Answer> answerList = await _dbContext.Answers.Where(x => x.QuestionId == Question).ToListAsync();
                foreach (var answer in answerList)
                {
                    answer.Status = answer.Status-1;
                }
                question.Status = 0;
                _dbContext.Answers.UpdateRange(answerList);
                _dbContext.Questions.Update(question);
                await _dbContext.SaveChangesAsync();
                return true;
            }
            return false;
        }

        public async Task<bool> EditQuestion(Question Question)
        {
            var question = await _dbContext.Questions.Where(x => x.Id == Question.Id).FirstOrDefaultAsync();
            if (question != null)
            {
                question.Question1 = Question.Question1;
                question.Mark = Question.Mark;
                _dbContext.Questions.Update(question);
                await _dbContext.SaveChangesAsync();
                return true;
            }
            return false;
        }

        public async Task<List<Question>> GetAllQuestions(int exerciseId)
        {
            var lessons = await _dbContext.Questions.Where(x => x.ExerciseId== exerciseId).ToListAsync();
            return lessons;
        }

        public async Task<Question> GetQuestionById(int? id)
        {
            return await _dbContext.Questions.Where(x => x.Id== id).FirstOrDefaultAsync();
        }
    }
}
