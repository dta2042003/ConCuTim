using Microsoft.EntityFrameworkCore;
using APIService.Service.Interface.Lessons;
using APIService.Entity;
using APIService.DataAccess;

namespace APIService.Service.Service.Lessons
{
    public class LessonService : ILessonService
    {
        private readonly TriolingoDbContext _dbContext;
        public LessonService(TriolingoDbContext dbContext)
        {
            _dbContext = dbContext;
        }
        //public async Task<User> FindExistLesson(string name)
        //{
        //    var userLoged = await _dbContext.Lessons.Where(x => x.Name == name && x.Status == 1).FirstOrDefaultAsync();
        //    return userLoged;
        //}

        public async Task<List<Lesson>> GetAllLesson()
        {
            var lessons = await _dbContext.Lessons.Where(x => x.Status == 1).ToListAsync();
            //var result = _mapper.Map<List<Lesson>>(lessons);
            return lessons;

        }
        public async Task<Lesson> GetLessonById(int id)
        {
            var lessons = await _dbContext.Lessons.Where(x => x.Status == 1 && x.Id == id).FirstOrDefaultAsync();
            return lessons;
        }

        public async Task<bool> AddLesson(Lesson lesson)
        {
            //var check = FindExistLesson(lesson.Name);
            bool check = false;
            if (check != null)
            {
                Lesson newUser = new Lesson()
                {
                    Name = lesson.Name,
                    Status = 1,
                    Note = lesson.Note,
                    Description = lesson.Description,
                    UnitId = lesson.UnitId,
                };
                await _dbContext.AddAsync(newUser);
                await _dbContext.SaveChangesAsync();

                return true;
            }
            return false;
        }

        public async Task<bool> UpdateLesson(Lesson lesson)
        {

            var lessons = await _dbContext.Lessons.Where(x => x.Status == 1 && x.Id == lesson.Id).FirstOrDefaultAsync();
            if (lessons != null)
            {
                lessons.Name = lesson.Name;
                lessons.Status = 1;
                lessons.Note = lesson.Note;
                lessons.Description = lesson.Description;
                lessons.UnitId = lesson.UnitId;
                //await context.Update(lessons);
                await _dbContext.SaveChangesAsync();
                return true;
            }
            return false;

        }

        public async Task<bool> DeleteLesson(int id)
        {
            var lessons = await _dbContext.Lessons.Where(x => x.Status == 1 && x.Id == id).FirstOrDefaultAsync();
            if (lessons != null)
            {
                //await context.RemoveAsync(lessons);
                await _dbContext.SaveChangesAsync();
            }
            return true;
        }

        public async Task<List<Lesson>> getAllLessonsByUnitId(int? unitId)
        {
            var lessons = await _dbContext.Lessons.Where(x => x.Status == 1 && x.UnitId == unitId).ToListAsync();
            return lessons;
        }
    }
}
