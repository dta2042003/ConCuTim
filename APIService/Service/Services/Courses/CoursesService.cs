using Microsoft.EntityFrameworkCore;
using System;
using APIService.Service.Interface.Courses;
using APIService.Entity;
using APIService.DataAccess;

namespace APIService.Service.Service.Courses
{
    public class CourseService : ICourseService
    {
        private readonly TriolingoDbContext _dbContext;
        public CourseService(TriolingoDbContext dBContext)
        {
            _dbContext = dBContext;
        }
        public async Task<int> AddNewCourse(Course Course)
        {
            await _dbContext.Courses.AddAsync(Course);
            await _dbContext.SaveChangesAsync();
            return Course.Id;
        }

        public async Task<bool> EditCourse(Course course)
        {
            var getCourse = await _dbContext.Courses.Where(x => x.Id == course.Id).FirstOrDefaultAsync();
            if (getCourse != null)
            {
                getCourse.Name=course.Name;
                getCourse.Description = course.Description;
                getCourse.Note= course.Note;
                getCourse.Status = course.Status;
                await _dbContext.SaveChangesAsync();
                return true;
            }
            return false;
        }

        public async Task<List<Course>> GetAllCourse()
        {
            var courses = await _dbContext.Courses.ToListAsync();
            //var result = _mapper.Map<List<Course>>(courses);
            return courses;
        }

        public async Task<Course> GetCourseById(int? id)
        {
            var course = await _dbContext.Courses.Where(x => x.Id == id).FirstOrDefaultAsync();
            //var result = _mapper.Map<Course>(course);
            return course;
        }
    }
}
