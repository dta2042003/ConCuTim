using Microsoft.EntityFrameworkCore;
using APIService.DataAccess;
using APIService.Entity;
using APIService.Service.Interface.QnA;

namespace APIService.Service.Service.QnA
{
    public class AnswerService : IAnswer
    {
        private readonly TriolingoDbContext _dbContext;
        public AnswerService(TriolingoDbContext dbContext)
        {
            _dbContext = dbContext;
        }

        public async Task<bool> ActiveAnswer(int Question)
        {
            Answer answer = await _dbContext.Answers.Where(x => x.Id == Question).FirstOrDefaultAsync();
            if (answer != null)
            {
                Question question = await _dbContext.Questions.Where(x => x.Id==Question).FirstOrDefaultAsync();
                if (question!=null&&question.Status != 1)
                {
                    Exercise exercise = await _dbContext.Exercises.Where(x => x.Id == question.ExerciseId).FirstOrDefaultAsync();
                    if(exercise != null && exercise.Status != 1)
                    {
                        exercise.Status = 1;
                        _dbContext.Exercises.Update(exercise);
                    }
                    question.Status = 1;
                    _dbContext.Questions.Update(question);
                }
                answer.Status = 1;
                _dbContext.Answers.Update(answer);
                await _dbContext.SaveChangesAsync();
                return true;
            }
            return false;
        }

        public async Task<bool> AddNewAnswer(Answer Answer)
        {
            await _dbContext.AddAsync(Answer);
            await _dbContext.SaveChangesAsync();
            return true;
        }

        public async Task<bool> DeleteAnswer(int Question)
        {
            Answer answer = await _dbContext.Answers.Where(x=>x.Id== Question).FirstOrDefaultAsync();
            if(answer != null)
            {
                answer.Status = 0;
                _dbContext.Answers.Update(answer);
                await _dbContext.SaveChangesAsync();
                return true;
            }
            return false;
        }

        public async Task<bool> EditAnswer(Answer Answer)
        {
            Answer answer = await _dbContext.Answers.Where(x => x.Id == Answer.Id).FirstOrDefaultAsync();
            if (answer != null)
            {
                answer.Answer1 = Answer.Answer1;
                answer.IsCorrect = Answer.IsCorrect;
                await _dbContext.SaveChangesAsync();
                return true;
            }
            return false;
        }

        public async Task<List<Answer>> GetAllAnswers(int questionId)
        {
            return await _dbContext.Answers.Where(x => x.QuestionId== questionId).ToListAsync();
        }

        public async Task<Answer> GetAnswerById(int? id)
        {
            return await _dbContext.Answers.Where(x => x.Id == id).FirstOrDefaultAsync();
        }
    }
}
