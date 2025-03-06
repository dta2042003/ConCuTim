using APIService.Entity;

namespace APIService.Service.Interface.QnA
{
    public interface IAnswer
    {
        Task<List<Answer>> GetAllAnswers(int questionId);
        Task<Answer> GetAnswerById(int? id);
        Task<bool> AddNewAnswer(Answer Answer);
        Task<bool> EditAnswer(Answer Answer);
        Task<bool> DeleteAnswer(int Question);
        Task<bool> ActiveAnswer(int Question);
    }
}
