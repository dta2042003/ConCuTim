using APIService.Entity;

namespace APIService.Service.Interface.QnA
{
    public interface IQuestion
    {
        Task<List<Question>> GetAllQuestions(int exerciseId);
        Task<Question> GetQuestionById(int? id);
        Task<int> AddNewQuestion(Question Question);
        Task<bool> EditQuestion(Question Question);
        Task<bool> DeleteQuestion(int Question);
        Task<bool> ActiveQuestion(int Question);
    }
}
