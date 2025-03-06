using Microsoft.EntityFrameworkCore;
using APIService.Service.Interface.Units;
using APIService.Entity;
using APIService.DataAccess;

namespace APIService.Service.Service.Units
{
    public class UnitService : IUnitService
    {
        private readonly TriolingoDbContext _dbContext;
        public UnitService(TriolingoDbContext dbContext)
        {
            _dbContext = dbContext;
        }
        public async Task<bool> AddUnit(Unit unit)
        {
            unit.Status = 1;
            await _dbContext.Units.AddAsync(unit);
            await _dbContext.SaveChangesAsync();
            return true;
        }

        public async Task<List<Unit>> GetAll()
        {
            return await _dbContext.Units.ToListAsync();
        }

        public async Task<Unit> GetById(int? unitId)
        {
            return await _dbContext.Units.Where(x => x.Id == unitId).FirstOrDefaultAsync();
        }

        public async Task<List<Unit>> GetUnitsByCourseId(int? courseId)
        {
            List<Unit> units = new List<Unit>();
            units = await _dbContext.Units.Where(x => x.CourseId == courseId).ToListAsync();
            return units;
        }

        public async Task<bool> UpdateUnit(Unit unit)
        {
            var item = await _dbContext.Units.SingleOrDefaultAsync(x => x.Id == unit.Id);
            if (item != null)
            {
                item.Order = unit.Order;
                item.Note = unit.Note;
                item.CourseId = unit.CourseId;
                item.Description = unit.Description;
                item.Name = unit.Name;
                _dbContext.Units.Update(item);
                await _dbContext.SaveChangesAsync();
                return true;
            }
            return false;
        }
    }
}
